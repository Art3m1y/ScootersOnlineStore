import React, { useEffect } from 'react'
import { Navigate } from 'react-router-dom'
import { Container, TextField, Alert, Box, Typography } from '@mui/material'
import LoadingButton from '@mui/lab/LoadingButton';
import { useForm, Controller, SubmitHandler } from "react-hook-form"
import { useAppDispatch } from '../../redux/hooks';
import { useAppSelector } from '../../redux/hooks';
import { z } from 'zod'
import { zodResolver } from "@hookform/resolvers/zod";

import { UserDataForRegistration } from '../../http/axios';
import { fetchRegistration, setRegistrationError, setRegistrationSuccess } from '../../redux/authReducer';
import FormControl from '@mui/material/FormControl'
import InputLabel from '@mui/material/InputLabel'
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem'



const formSchema = z.object({
    name: z.string().min(1, { message: 'Обязательное поле' }).max(30, { message: 'max length is 30' }),
    surname: z.string().min(1, { message: 'Обязательное поле' }).max(30, { message: 'max length is 30' }),
    yearOfBirth: z.number().min(1, { message: 'Обязательное поле' }),
    country: z.string().min(1, { message: 'Обязательное поле' }),
    email: z.string().email('Введите корректный email').max(50, { message: 'max length is 50' }),
    password: z.string().min(1, { message: "Обязательное поле" })
})


const RegistrationFormMUI: React.FC<{}> = () => {
    useEffect(() => {
        return () => {
            dispatch(setRegistrationError(null))
            dispatch(setRegistrationSuccess(true))
        }
    }, [])


    const isAuth = useAppSelector(state => state.auth.isAuth)
    const axiosError = useAppSelector(state => state.auth.registrationError)
    const success = useAppSelector(state => state.auth.registrationSuccess)
    let loading = useAppSelector(state => state.auth.loading)
    const dispatch = useAppDispatch()
    const {
        control,
        formState: {
            errors,
        },
        handleSubmit,
    } = useForm<UserDataForRegistration>({
        defaultValues: {
            name: '',
            surname: '',
            yearOfBirth: 2022,
            country: "",
            email: '',
            password: '',
        },
        resolver: zodResolver(formSchema)
    });

    const onSubmit: SubmitHandler<UserDataForRegistration> = (data) => {

        let regData = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
        let formData = new FormData()
        formData.append('register', regData)
        dispatch(fetchRegistration(formData))
    }
    if (isAuth) {
        return <Navigate to='/' />
    }

    return (
        <Container maxWidth="sm" sx={{ height: '90vh' }}>

            <Typography variant='h4'>Регистрация</Typography>
            <Box
                sx={{ verticalAlign: 'middle' }}
                component="form" onSubmit={handleSubmit(onSubmit)}>
                <Controller
                    name="name"
                    control={control}
                    rules={{ required: true }}
                    render={({ field }) => <TextField {...field}
                        error={!!errors.name}
                        helperText={errors.name ? errors.name?.message : ''}
                        label="Name" variant='outlined'
                        fullWidth margin='dense' />}
                />
                <Controller
                    name="surname"
                    control={control}
                    rules={{ required: true }}
                    render={({ field }) => <TextField {...field}
                        type="surname"
                        error={!!errors.surname}
                        helperText={errors.surname ? errors.surname?.message : ''}
                        label="Surname" variant='outlined'
                        fullWidth
                        margin='dense' />}
                />
                <Controller
                    name="email"
                    control={control}
                    rules={{ required: true }}
                    render={({ field }) => <TextField {...field}
                        error={!!errors.email}
                        helperText={errors.email ? errors.email?.message : ''}
                        label="Email" variant='outlined'
                        fullWidth margin='dense' />}
                />
                <Controller
                    name="password"
                    control={control}
                    rules={{ required: true }}
                    render={({ field }) => <TextField {...field}
                        type="password"
                        error={!!errors.password}
                        helperText={errors.password ? errors.password?.message : ''}
                        label="Password" variant='outlined'
                        fullWidth
                        margin='dense' />}
                />
                <Controller
                    name="yearOfBirth"
                    control={control}
                    render={({ field: { onChange, value } }) => <FormControl fullWidth margin='dense'>
                        <InputLabel id="age-label">Year of birth</InputLabel>
                        <Select
                            id="age-select"
                            value={value}
                            label="Age"
                            onChange={onChange}>
                            <MenuItem value={1976}>1976</MenuItem>
                            <MenuItem value={1999}>1999</MenuItem>
                            <MenuItem value={2000}>2000</MenuItem>
                            <MenuItem value={2022}>2022</MenuItem>
                        </Select>
                    </FormControl>

                    }
                />
                <Controller
                    name="country"
                    control={control}
                    render={({ field: { onChange, value } }) => <FormControl fullWidth margin='dense'>
                        <InputLabel id="country-select">Country</InputLabel>
                        <Select
                            labelId="country-select"
                            id="country-select"
                            value={value}
                            label="Country"
                            onChange={onChange}>
                            <MenuItem value="Afghanistan">Afghanistan</MenuItem>
                            <MenuItem value="Åland Islands">Åland Islands</MenuItem>
                            <MenuItem value="Albania">Albania</MenuItem>
                            <MenuItem value="Algeria">Algeria</MenuItem>
                            <MenuItem value="American Samoa">American Samoa</MenuItem>
                            <MenuItem value="Andorra">Andorra</MenuItem>
                            <MenuItem value="Angola">Angola</MenuItem>
                            <MenuItem value="Anguilla">Anguilla</MenuItem>
                            <MenuItem value="Antarctica">Antarctica</MenuItem>
                            <MenuItem value="Antigua and Barbuda">Antigua and Barbuda</MenuItem>
                            <MenuItem value="Argentina">Argentina</MenuItem>
                            <MenuItem value="Armenia">Armenia</MenuItem>
                            <MenuItem value="Aruba">Aruba</MenuItem>
                            <MenuItem value="Australia">Australia</MenuItem>
                            <MenuItem value="Austria">Austria</MenuItem>
                            <MenuItem value="Azerbaijan">Azerbaijan</MenuItem>
                            <MenuItem value="Bahamas">Bahamas</MenuItem>
                            <MenuItem value="Bahrain">Bahrain</MenuItem>
                            <MenuItem value="Bangladesh">Bangladesh</MenuItem>
                            <MenuItem value="Barbados">Barbados</MenuItem>
                            <MenuItem value="Belarus">Belarus</MenuItem>
                        </Select>
                    </FormControl>

                    }
                />


                <LoadingButton loading={loading} type='submit' variant='contained' sx={{ mt: 2 }}>Зарегистрироваться</LoadingButton>
            </Box>
            {axiosError && <Alert sx={{ mt: 2 }} variant='outlined' severity="error">{axiosError}</Alert>}
            {success && <Alert sx={{ mt: 2 }} variant='outlined' severity="info">
                Registration was completed successful, check your email for confirmation
            </Alert>}
        </Container>
    )
}

export default RegistrationFormMUI;