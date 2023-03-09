import React, { memo } from 'react'
import { Box, Menu, MenuItem, IconButton, Avatar, Link, Typography } from '@mui/material'
import { useAppSelector, useAppDispatch } from '../../redux/hooks';
import { fetchLogout } from '../../redux/authReducer';
import { useNavigate } from 'react-router-dom';
import FavoriteIcon from '@mui/icons-material/Favorite';


export const UserMenu: React.FC<{}> = memo(() => {

    const isAuth = useAppSelector(store => store.auth.isAuth)
    const userData = useAppSelector(store => store.auth)
    const avatar = `https://shop.javaspringbackend.software/avatar/${userData.id}`
    console.log(avatar)

    const dispatch = useAppDispatch()
    const logout = () => {
        dispatch(fetchLogout())
    }
    let nav = useNavigate()

    const [anchorElUserMenu, setAnchorElUserMenu] = React.useState<null | HTMLElement>(null);
    const openUserMenu = Boolean(anchorElUserMenu);
    const handleCloseUserMenu = () => {
        setAnchorElUserMenu(null);
    };
    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUserMenu(event.currentTarget);
    };


    return (
        <>
            <IconButton onClick={handleOpenUserMenu}>
                {userData.username ?
                    <Avatar sx={{ ml: 1 }} src={avatar} alt={userData.username}></Avatar>
                    :
                    <Avatar sx={{ ml: 1 }} ></Avatar>
                }
            </IconButton>
            <Menu
                anchorEl={anchorElUserMenu}
                open={openUserMenu}
                onClose={handleCloseUserMenu}
            >
                <MenuItem
                    onClick={() => nav('/favorites')}
                    sx={{ display: { xs: 'inline-flex', md: 'none' } }}
                >
                    <Typography>Favorites</Typography>
                    <IconButton
                        color="primary"
                        aria-label="open favorites"
                    >
                        <FavoriteIcon fontSize="large" />
                    </IconButton>
                </MenuItem>
                {isAuth ?
                    <Box>
                        <MenuItem onClick={() => {
                            handleCloseUserMenu()
                            logout()
                        }}>
                            <Link underline="hover" color='inherit'>Logout</Link>
                        </MenuItem>
                        <MenuItem onClick={() => {
                            handleCloseUserMenu()
                            nav('/profile')
                        }}>
                            <Link underline="hover" color='inherit'>Profile</Link>
                        </MenuItem>
                    </Box>


                    :
                    <Box>
                        <MenuItem onClick={() => {
                            handleCloseUserMenu()
                            nav('/login')
                        }} >
                            <Link underline="hover" color='inherit'>Войти</Link>
                        </MenuItem>


                        <MenuItem onClick={() => {
                            handleCloseUserMenu()
                            nav('/registration')
                        }}>
                            <Link underline="hover" color='inherit'>Регистрация</Link>
                        </MenuItem>
                    </Box>
                }
                <MenuItem onClick={() => {
                    handleCloseUserMenu()
                    nav('/admin')
                }}>
                    <Link underline="hover" color='inherit'>Admin panel</Link>
                </MenuItem>
            </Menu>
        </>
    )
})