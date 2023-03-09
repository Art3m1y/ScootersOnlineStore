import { Stack, Skeleton, Container, Box } from '@mui/material'


export const ProductPageSkeleton: React.FC<{}> = () => {
    return (
        <Container sx={{ my: 2 }}>
            <Stack spacing={3} justifyContent="center" alignItems={{ xs: 'center', md: 'start' }} direction={{ xs: 'column', md: 'row' }} sx={{ mb: 2 }}>
                <Skeleton animation='wave' variant='rounded' width={564} height={400} />
                <Box sx={{ width: { xs: '100%', sm: 500, md: '100%' }, maxWidth: 425 }}>

                    <Skeleton variant='text' sx={{ fontSize: '2.125rem' }} />
                    <Skeleton variant='text' sx={{ fontSize: '2.125rem' }} width={200} />
                    <Skeleton variant='text' sx={{ fontSize: '2.125rem' }} width={250} />
                    <Skeleton variant='text' height={200} />
                    <Stack spacing={2} justifyContent="center" sx={{ my: 3 }} direction="row">
                        <Skeleton animation='wave' variant='rounded' width={140} height={40} />
                        <Skeleton animation='wave' variant='rounded' width={140} height={40} />
                    </Stack>
                </Box>
            </Stack>
        </Container>
    )
}