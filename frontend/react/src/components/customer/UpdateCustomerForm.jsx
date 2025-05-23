import {Form, Formik, useField} from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack, VStack,Icon} from "@chakra-ui/react";
import {customerProfilePictureUrl, saveCustomer, updateCustomer, uploadCustomerProfilePicture} from "../../services/client.js";
import {successNotification, errorNotification} from "../../services/notification.js";
import {useDropzone} from 'react-dropzone';
import React, { useCallback } from 'react';
import { Image } from '@chakra-ui/react';
import { useEffect } from 'react';
import { FaUserAlt } from 'react-icons/fa';


const MyTextInput = ({label, ...props}) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const MyDropzone = ({customerId,fetchCustomers}) => {
    const onDrop = useCallback((acceptedFiles) => {
        console.log(acceptedFiles);
        const formData = new FormData();
        formData.append("file", acceptedFiles[0]);

        uploadCustomerProfilePicture(
            customerId, 
            formData
        ).then(res => {
            successNotification("Success","Profile Picture uploaded successfully");
            fetchCustomers();
        }).catch((err) =>{
            errorNotification("Error", "Error uploading profile picture");
        })
    }, []);
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})
    return (
        <Box {...getRootProps()}
            w={"100%"}
            textAlign={"center"}
            border={"dashed"}
            borderColor={"gray.200"}
            borderRadius={"3xl"}
            p={6}
            rounded={"md"}
        >
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop the picture here ...</p> :
                    <p>Drag 'n' drop picture here, or click to select picture</p>
            }
        </Box>
    )
}

// And now we can use these
const UpdateCustomerForm = ({ fetchCustomers, initialValues, customerId }) => {
    useEffect(() => {
        const url = customerProfilePictureUrl(customerId);
        console.log("URL de la imagen de perfil:", url);
      }, [customerId]);
    return (
        <>
            <VStack spacing={'5'} mb={'5'}>
                <Image
                    alt="...Image   "
                    borderRadius="full"
                    boxSize="150px"
                    objectFit="cover"
                    objectPosition="center"
                    src={customerProfilePictureUrl(customerId)}
                />
                <MyDropzone customerId={customerId} fetchCustomers={fetchCustomers}/>
            </VStack>
            <Formik
                initialValues={initialValues}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, 'Must be 15 characters or less')
                        .required('Required'),
                    email: Yup.string()
                        .email('Must be 20 characters or less')
                        .required('Required'),
                    age: Yup.number()
                        .min(16, 'Must be at least 16 years of age')
                        .max(100, 'Must be less than 100 years of age')
                        .required(),
                })}
                onSubmit={(updatedCustomer, {setSubmitting}) => {
                    setSubmitting(true);
                    updateCustomer(customerId, updatedCustomer)
                        .then(res => {
                            console.log(res);
                            successNotification(
                                "Customer updated",
                                `${updatedCustomer.name} was successfully updated`
                            )
                            fetchCustomers();
                        }).catch(err => {
                            console.log(err);
                            errorNotification(
                                err.code,
                                err.response.data.message
                            )
                    }).finally(() => {
                         setSubmitting(false);
                    })
                }}
            >
                {({isValid, isSubmitting, dirty}) => (
                    <Form>
                        <Stack spacing={"24px"}>
                            <MyTextInput
                                label="Name"
                                name="name"
                                type="text"
                                placeholder="Jane"
                            />

                            <MyTextInput
                                label="Email Address"
                                name="email"
                                type="email"
                                placeholder="jane@formik.com"
                            />

                            <MyTextInput
                                label="Age"
                                name="age"
                                type="number"
                                placeholder="20"
                            />

                            <Button disabled={!(isValid && dirty) || isSubmitting} type="submit">Submit</Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
};

export default UpdateCustomerForm;