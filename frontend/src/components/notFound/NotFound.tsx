import React from 'react'
import { Container, Typography } from '@mui/material';
import SentimentVeryDissatisfiedIcon from '@mui/icons-material/SentimentVeryDissatisfied';


const NotFound = () => {
    return (
        <Container maxWidth='xl' sx={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', gap: 1, minHeight: '70vh', mt: 5 }}>
            <Typography color='primary' variant='h2'>404 Not found </Typography>
            <SentimentVeryDissatisfiedIcon color='primary' sx={{ fontSize: 55, pt: 1 }} />
        </Container>
    )
}

export default NotFound