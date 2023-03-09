import React from 'react'
import { Container, Avatar, Rating, IconButton } from '@mui/material/'
import { Stack } from '@mui/system'
import Typography from '@mui/material/Typography'
import { CommentType } from '../../redux/scootersCatalogReducer'
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import { useAppDispatch, useAppSelector } from '../../redux/hooks'
import { deleteComment } from '../../redux/scootersCatalogReducer'

interface CommentProps extends CommentType {
    productId: number
}

export const Comment: React.FC<CommentProps> = ({ person, text, mark, id, productId }) => {

    const dispatch = useAppDispatch()
    const userId = useAppSelector(state => state.auth.id)
    const userRole = useAppSelector(state => state.auth.role)

    const deleteThisComment = () => {
        dispatch(deleteComment({ commentId: id, productId }))
    }
    return (
        <Container sx={{ mt: 2, position: 'relative' }}>
            <Stack direction='row' alignItems='center'>
                <Avatar sx={{ width: 56, height: 56 }} src={`https://shop.javaspringbackend.software/avatar/${person.id}`}>{person.name[0]}</Avatar>
                <Stack sx={{ ml: 1 }} direction='column'>
                    <Typography variant='body1' component='p'>{person.name + ' ' + person.surname}</Typography>
                    <Rating value={+mark} precision={0.5} readOnly />
                </Stack>
            </Stack>
            <Typography variant='body1' component='p'>{text}</Typography>
            {(userId === person.id || userRole === 'ROLE_ADMIN') &&
                <IconButton onClick={deleteThisComment} color="primary" sx={{ position: 'absolute', top: -10, right: -10 }}>
                    <DeleteOutlineIcon />
                </IconButton>
            }
        </Container>
    )
}