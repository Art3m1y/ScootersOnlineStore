import React, { useEffect } from 'react'
import { Container, Typography, Link, Stack, Alert, AlertTitle, CircularProgress } from '@mui/material'
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { Navigate } from 'react-router-dom'
import { useParams } from 'react-router-dom'

import { useNavigate } from 'react-router-dom';
import { fetchVerification } from '../../redux/authReducer';


const Verification: React.FC<{}> = () => {


    const dispatch = useAppDispatch()
    let nav = useNavigate()
    let isAuth = useAppSelector(state => state.auth.isAuth)
    let isSuccess = useAppSelector(state => state.auth.verificationSuccess)
    let errorMessage = useAppSelector(state => state.auth.verificationErrorMessage)
    let isLoading = useAppSelector(state => state.auth.loading)

    let { key } = useParams<{ key: string }>()

    useEffect(() => {
        if (key) {
            dispatch(fetchVerification(key))
        }
    }, [dispatch, key])

    if (isAuth) {
        return <Navigate to='/' />
    }

    return (
        <Container>
            <Typography variant='h4'>Verification</Typography>
            <Stack spacing={2} sx={{ maxWidth: 700, mx: 'auto', my: 2 }} alignItems='center'>
                {isLoading ? <CircularProgress /> :

                    isSuccess ?
                        <>
                            <Alert severity="success" variant='outlined' sx={{ fontSize: 20 }}>
                                <AlertTitle sx={{ fontSize: 30 }}>Success</AlertTitle>
                                Your account is activated, now you can log in
                            </Alert>
                            <Link onClick={() => nav('/login')} underline="always" color='inherit' variant='h5' sx={{ cursor: 'pointer' }}>Go to login</Link>
                        </>
                        :
                        <Alert severity="error" variant='outlined' sx={{ fontSize: 20 }}>
                            <AlertTitle sx={{ fontSize: 30 }}>Error</AlertTitle>
                            Error message: {errorMessage}<br />
                            Try again
                        </Alert>
                }

            </Stack>
        </Container>
    )
}

export default Verification