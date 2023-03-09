import React, { memo } from 'react';
import { Card, CardActions, CardContent, CardMedia, IconButton, Typography, Paper, Grid, Rating } from '@mui/material';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import FavoriteIcon from '@mui/icons-material/Favorite';
import BatteryChargingFullIcon from '@mui/icons-material/BatteryChargingFull';
import BoltIcon from '@mui/icons-material/Bolt';
import SpeedIcon from '@mui/icons-material/Speed';
import TimerOutlinedIcon from '@mui/icons-material/TimerOutlined';
import { styled } from '@mui/material/styles';
import { Scooter } from '../../redux/scootersCatalogReducer';
import { NavLink } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { addItemToCartLocal, addOneItemToCart } from '../../redux/cartReducer';
import { addItemToFavorites } from '../../redux/favoritesReducer';

const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.background.paper,
    ...theme.typography.body2,
    padding: 0,
    display: 'flex',
    gap: 1,
    textAlign: 'start',
    alignItems: 'center',
    color: theme.palette.text.secondary,
    boxShadow: 'none',
}));


export const ProductCardMUI: React.FC<Scooter> = memo((scooterProps) => {
    const reserveImage = 'https://shop.by/images/mizar_senator_sungate_1.jpg'
    const dispatch = useAppDispatch()
    const isAuth = useAppSelector(state => state.auth.isAuth)
    const inFavorites = useAppSelector(store => store.favorites.favoritesItems.findIndex(item => item.id === scooterProps.id))
    let sendToCart = () => {
        if (isAuth) {
            dispatch(addOneItemToCart({ productId: scooterProps.id, amount: 1 }))
        } else {
            dispatch(addItemToCartLocal({ product: { ...scooterProps }, amount: 1 }))
        }
    }
    let sendToFavorites = () => {
        dispatch(addItemToFavorites({ ...scooterProps }))
    }

    return (
        <Card sx={{ width: 245, height: 466.5, boxShadow: 'none' }}>
            <NavLink to={`/catalog/${scooterProps.id}`}>
                <CardMedia
                    sx={{ height: 245 }}
                    image={scooterProps.images[0] !== undefined ? scooterProps.images[0].link : reserveImage}
                    title={`${scooterProps.name}`}
                />
            </NavLink>
            <CardContent sx={{ padding: 0, px: 3 }}>
                <Typography gutterBottom variant="h6" component="div" sx={{ fontWeight: 'bold', height: '64px' }}>
                    {scooterProps.name}
                </Typography>
                <Rating defaultValue={+scooterProps.mark} precision={0.5} readOnly />
                <Grid container spacing={1}>
                    <Grid item xs={6}>
                        <Item>
                            <BatteryChargingFullIcon /> {scooterProps.batteryCapacity} Ah
                        </Item>
                    </Grid>
                    <Grid item xs={6}>
                        <Item>
                            <BoltIcon /> {scooterProps.power} л/c
                        </Item>
                    </Grid>
                    <Grid item xs={6}>
                        <Item>
                            <SpeedIcon sx={{ marginRight: '3px' }} /> {scooterProps.speed} км/ч
                        </Item>
                    </Grid>
                    <Grid item xs={6}>
                        <Item>
                            <TimerOutlinedIcon sx={{ marginRight: '3px' }} /> {scooterProps.time} ч
                        </Item>
                    </Grid>
                </Grid>
                <CardActions sx={{ padding: 0 }}>
                    <Typography variant="h6" component='p' sx={{ fontWeight: 'bold', flexGrow: 1 }}>
                        {scooterProps.cost}₽
                    </Typography>
                    <IconButton onClick={sendToCart} color="primary" aria-label="add to shopping cart">
                        <AddShoppingCartIcon />
                    </IconButton>
                    <IconButton onClick={sendToFavorites} color="primary" aria-label="add favorite">
                        {inFavorites !== -1 ?
                            <FavoriteIcon /> :
                            <FavoriteBorderIcon />
                        }
                    </IconButton>
                </CardActions>
            </CardContent>
        </Card>
    );
})