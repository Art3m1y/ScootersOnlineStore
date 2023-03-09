import React from 'react'
import { Box, Card, CardContent, CardMedia, IconButton, Typography } from '@mui/material';

import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import ClearIcon from '@mui/icons-material/Clear';
import { useAppDispatch, useAppSelector } from '../../redux/hooks'
import { useNavigate } from 'react-router-dom';
import { addItemToCartLocal, reduceItemCountInCartLocal, deleteItemFromCartLocal, addOneItemToCart, deleteItemFromCart } from '../../redux/cartReducer'

import { ScooterForCart } from '../../redux/cartReducer';


export const CartItem: React.FC<ScooterForCart> = (scooterProps) => {
    let isAuth = useAppSelector(state => state.auth.isAuth)
    const reserveImage = 'https://shop.by/images/mizar_senator_sungate_1.jpg'
    const dispatch = useAppDispatch()
    const add = () => {
        if (isAuth) {
            dispatch(addOneItemToCart({ productId: scooterProps.product.id, amount: 1 }))
        } else {
            dispatch(addItemToCartLocal({ ...scooterProps }))
        }

    }
    const reduce = () => {
        if (isAuth) {
            dispatch(deleteItemFromCart({ productId: scooterProps.product.id, isAll: false }))
        } else {
            dispatch(reduceItemCountInCartLocal({ ...scooterProps }))
        }
    }
    const deleteItem = () => {
        if (isAuth) {
            dispatch(deleteItemFromCart({ productId: scooterProps.product.id, isAll: true }))
        } else {
            dispatch(deleteItemFromCartLocal({
                id: scooterProps.product.id,
                cost: scooterProps.product.cost,
                number: scooterProps.amount
            }))
        }

    }

    const nav = useNavigate()


    return (
        <Card sx={{ position: 'relative', display: 'flex', justifyContent: 'center', mb: 2, flexWrap: 'wrap' }}>
            <CardMedia
                sx={{ height: { xs: 175, sm: 110 }, width: { xs: 245, sm: 110 }, marginRight: 1, cursor: 'pointer' }}
                onClick={() => nav(`/catalog/${scooterProps.product.id}`)}
                image={scooterProps.product.images[0] !== undefined ? scooterProps.product.images[0].link : reserveImage}
                title="scooter"
            />
            <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' } }}>
                <CardContent sx={{ px: 1, py: 0, minWidth: '222px' }}>
                    <Typography onClick={() => nav(`/catalog/${scooterProps.product.id}`)}
                        variant="h6"
                        component="div"
                        sx={{ fontWeight: 'bold', cursor: 'pointer', '&:hover': { color: `primary.main` } }}>
                        {scooterProps.product.name}
                    </Typography>
                    <Typography variant="body2" component="div" >
                        Ёмкость аккумулятора: {scooterProps.product.batteryCapacity} Ah
                    </Typography>
                    <Typography variant="body2" component="div" >
                        Максимальная скорость: {scooterProps.product.speed} км/ч
                    </Typography>
                    <Typography variant="body2" component="div" >
                        Мощьность двигателя: {scooterProps.product.power} л/с
                    </Typography>
                    <Typography variant="body2" component="div" >
                        Работа без подзарядки: {scooterProps.product.time} ч
                    </Typography>
                </CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mx: 2, justifyContent: 'center' }}>
                    <IconButton onClick={reduce} disabled={scooterProps.amount === 1} color="primary">
                        <RemoveIcon />
                    </IconButton>
                    <Typography variant="h6" component="div" sx={{ fontWeight: 'bold', width: '35px', textAlign: 'center' }}>
                        {scooterProps.amount}
                    </Typography>
                    <IconButton onClick={add} color="primary">
                        <AddIcon />
                    </IconButton>
                </Box>
                <Box sx={{ display: 'flex', alignItems: 'center', mx: 2 }}>
                    <Typography gutterBottom variant="h6" component="div" align='center' sx={{ fontWeight: 'bold', width: '100px', margin: '0 auto' }}>
                        {+scooterProps.product.cost * scooterProps.amount}₽
                    </Typography>
                </Box>

                <IconButton onClick={deleteItem} color="primary" sx={{ position: 'absolute', top: -10, right: -10 }}>
                    <ClearIcon />
                </IconButton>

            </Box>
        </Card>
    )
}