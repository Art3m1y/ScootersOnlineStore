import React from 'react'
import { Container, Typography, Pagination } from '@mui/material';
import { useAppSelector } from '../../redux/hooks';
import { ProductCardMUI } from '../productCard/ProductCardMUI';


const Favorites: React.FC<{}> = () => {
    let items = useAppSelector(store => store.favorites.favoritesItems)
    let favoritesBody = items.map(item => <ProductCardMUI key={item.id} {...item} />)
    return (
        <>
            <Container sx={{ mt: 3 }}>
                <Typography gutterBottom variant='h4'>Favorites</Typography>
            </Container>
            <Pagination
                count={1}
                color="primary"
                page={1}
                sx={{ width: 'fit-content', mx: 'auto', my: 2 }} />
            <Container maxWidth='xl' sx={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', gap: 3, minHeight: '70vh' }}>
                {favoritesBody}
            </Container>
        </>
    )
}

export default Favorites