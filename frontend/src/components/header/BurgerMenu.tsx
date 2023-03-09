import React, { useState } from 'react'
import { Button, Box, IconButton, Menu, MenuItem, Typography } from '@mui/material'
import { useNavigate } from 'react-router-dom';
import MenuIcon from '@mui/icons-material/Menu';
import MenuOpenIcon from '@mui/icons-material/MenuOpen';
import { SearchForm } from '../searchForm/SearchForm';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { toggleTheme } from '../../redux/appReducer';
import { ThemeSwitch } from './ThemeSwitch';

export const BurgerMenu: React.FC<{}> = () => {

    const mod = useAppSelector(store => store.app.theme)
    const nav = useNavigate()
    const dispatch = useAppDispatch()

    const [anchorElBurgerMenu, setAnchorElBurgerMenu] = useState<null | HTMLElement>(null);
    const openBurgerMenu = Boolean(anchorElBurgerMenu);
    const handleOpenBurgerMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElBurgerMenu(event.currentTarget);
    };
    const handleCloseBurgerMenu = () => {
        setAnchorElBurgerMenu(null);
    };

    const changeTheme = () => {
        dispatch(toggleTheme())
    }



    return (
        <>
            <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
                <IconButton onClick={handleOpenBurgerMenu} size="large">
                    <MenuIcon color='primary' />
                </IconButton>
            </Box>
            <Menu
                anchorEl={anchorElBurgerMenu}
                open={openBurgerMenu}
                onClose={handleCloseBurgerMenu}
            >
                <Box>
                    <MenuItem>
                        <SearchForm />
                    </MenuItem>
                    <MenuItem onClick={handleCloseBurgerMenu}>
                        <Button onClick={() => nav('/')} variant="contained" fullWidth endIcon={<MenuOpenIcon />}>
                            Catalog
                        </Button>
                    </MenuItem>
                    <MenuItem sx={{ justifyContent: 'center' }}>
                        <Typography>Light</Typography>
                        <ThemeSwitch onChange={changeTheme} sx={{ m: 1 }} checked={mod === 'light' ? false : true} />
                        <Typography>Dark</Typography>
                    </MenuItem>
                </Box>
            </Menu>
        </>
    )
}