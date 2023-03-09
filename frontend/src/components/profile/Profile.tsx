import React, { useEffect, useState } from 'react'
import { Avatar, Box, Button, Container, Stack, TextField, Typography } from '@mui/material'
import { useAppDispatch, useAppSelector } from '../../redux/hooks'
import { useForm, Controller, SubmitHandler } from "react-hook-form"
import { z } from 'zod'
import { zodResolver } from "@hookform/resolvers/zod";
import { fetchUpdateProfile } from '../../redux/authReducer'


const MAX_FILE_SIZE = 524000;
const ACCEPTED_IMAGE_TYPES = ["image/jpeg", "image/jpg", "image/png", "image/webp"];
const formSchema = z.object({
    name: z.string().min(1, { message: "Обязательное поле" }).max(30, { message: 'max length is 30' }),
    surname: z.string().min(1, { message: "Обязательное поле" }).max(30, { message: 'max length is 30' }),
    avatar: z
        .any()
        .refine((files) => files?.[0]?.size > 0, `Upload image`)
        .refine((files) => files?.[0]?.size <= MAX_FILE_SIZE, `Max image size is 500KB.`)
        .refine(
            (files) => ACCEPTED_IMAGE_TYPES.includes(files?.[0]?.type),
            "Only .jpg, .jpeg, .png and .webp formats are supported."
        ),
})
type UpdateProfileFormData = z.infer<typeof formSchema>

const Profile: React.FC<{}> = () => {

    const userData = useAppSelector(state => state.auth)
    const dispatch = useAppDispatch()

    const [selectedFile, setSelectedFile] = useState()
    const [preview, setPreview] = useState<string>()


    const {
        control,
        register,
        setValue,
        formState: {
            errors,
        },
        handleSubmit,
    } = useForm<UpdateProfileFormData>({
        defaultValues: {
            name: userData.username ? userData.username : '',
            surname: userData.surname ? userData.surname : ''
        },
        resolver: zodResolver(formSchema)
    });
    useEffect(() => {
        if (userData) {
            setValue('name', userData.username ? userData.username : '');
            setValue('surname', userData.surname ? userData.surname : '');
        }
    }, [userData, setValue]);

    const onSubmit: SubmitHandler<UpdateProfileFormData> = (data) => {
        let product = {
            name: userData.username,
            surname: userData.surname,
            email: userData.email,
            yearOfBirth: '2022',
            country: "Afghanistan",
        }
        let productData = new Blob([JSON.stringify(product, null, 2)], { type: 'application/json' })
        let formData = new FormData()
        formData.append('person', productData)
        formData.append('avatar', data.avatar[0])
        console.log(formData)
        dispatch(fetchUpdateProfile(formData))
    }

    // create a preview as a side effect, whenever selected file is changed
    useEffect(() => {
        if (!selectedFile) {
            setPreview(undefined)
            return
        }
        const objectUrl = URL.createObjectURL(selectedFile)
        setPreview(objectUrl)

        // free memory when ever this component is unmounted
        return () => URL.revokeObjectURL(objectUrl)
    }, [selectedFile])

    const onSelectFile = (e: any) => {
        if (!e.target.files || e.target.files.length === 0) {
            setSelectedFile(undefined)
        }
        setSelectedFile(e.target.files[0])
    }

    return (
        <Container>
            <Typography gutterBottom sx={{ mt: 3 }} variant='h4'> Profile</Typography>
            <Stack spacing={4} sx={{ width: 'fit-content', m: '0 auto' }} justifyContent='center' direction={{ xs: 'column', sm: 'row' }}>

                <Stack>
                    <Avatar
                        sx={{ width: 250, height: 250, m: '0 auto' }}
                        src={preview ? preview : `https://shop.javaspringbackend.software/avatar/${userData.id}`}
                        variant='rounded'></Avatar>
                    <Stack alignItems="center">
                        <Button sx={{ mt: 2 }} variant="contained" component="label">
                            Upload image
                            <input hidden onChangeCapture={onSelectFile} accept="image/*" multiple type="file" {...register("avatar")} />
                        </Button>
                    </Stack>
                </Stack>
                <Box
                    component="form"
                    onSubmit={handleSubmit(onSubmit)}
                    sx={{ display: 'flex', flexDirection: 'column', width: 300 }}>
                    <Controller
                        name='name'
                        control={control}
                        rules={{ required: true }}
                        render={({ field }) => <TextField {...field}
                            type="text"
                            error={!!errors.name}
                            helperText={errors.name ? errors.name?.message : ''}
                            label="Name" variant='outlined'
                            fullWidth
                            margin='dense' />}
                    />
                    <Controller
                        name='surname'
                        control={control}
                        rules={{ required: true }}
                        render={({ field }) => <TextField {...field}
                            type="text"
                            error={!!errors.name}
                            helperText={errors.name ? errors.name?.message : ''}
                            label="Surname" variant='outlined'
                            fullWidth
                            margin='dense' />}
                    />

                    <Button type='submit' sx={{ mt: 2 }} variant='contained' >Save</Button>

                </Box>
            </Stack>
        </Container>
    )
}

export default Profile;