import React from 'react'
import { Container, Typography, Grid, Box, Button } from '@mui/material'
import { CartItem } from '../cartItem/CartItem'
import { useAppDispatch, useAppSelector } from '../../redux/hooks'
import { clearCartOnServer, clearCartState } from '../../redux/cartReducer'

const Cart: React.FC<{}> = () => {

    const dispatch = useAppDispatch()
    let totalCost = useAppSelector(state => state.cart.totalCost)
    let cartItems = useAppSelector(state => state.cart.cartItems)
    let isAuth = useAppSelector(state => state.auth.isAuth)
    const clearCartHandler = () => {
        if (isAuth) {
            dispatch(clearCartOnServer())
        } else {
            dispatch(clearCartState())
        }
    }

    return (
        <Container >
            <Typography sx={{ mt: 3 }} gutterBottom variant='h4'>Cart</Typography>
            <Button sx={{ mb: 2 }} variant='outlined' onClick={clearCartHandler}>Clear cart</Button>
            <Grid container spacing={2}>
                <Grid item sx={{ mb: 2 }} xs={12} md={9}>
                    {cartItems.map(item => <CartItem key={item.product.id}  {...item} />)}

                </Grid>
                <Grid item sx={{ mb: 4 }} xs={12} md={3}>
                    <Box sx={{ display: 'flex', alignItems: 'center', mx: 2 }}>
                        <Typography gutterBottom variant="h5" component="span" sx={{ fontWeight: 'bold', flexGrow: 1 }}>
                            Итого:
                        </Typography>
                        <Typography gutterBottom variant="h5" component="span" sx={{ fontWeight: 'bold' }}>
                            {totalCost} ₽
                        </Typography>
                    </Box>
                    <Button variant='contained' fullWidth>Заказать</Button>
                </Grid>
            </Grid>


        </Container>
    )
}

export default Cart;