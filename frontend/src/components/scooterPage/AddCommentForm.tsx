import React from 'react'
import { Rating, Button, Box, TextField, Typography } from '@mui/material'
import { useForm, Controller, SubmitHandler } from 'react-hook-form';
import { AddCommentData } from '../../http/axios';
import { useAppDispatch } from '../../redux/hooks';
import { addComment } from '../../redux/scootersCatalogReducer';


export const AddCommentForm: React.FC<{ id: number }> = ({ id }) => {

    const dispatch = useAppDispatch()


    const [rating, setRating] = React.useState<number | null>(0);

    const {
        control,
        formState: { errors },
        handleSubmit,
        reset
    } = useForm<AddCommentData>({
        defaultValues: {
            text: '',
        },
    });

    const onSubmit: SubmitHandler<AddCommentData> = (data: any) => {
        data.mark = rating
        data.id = id
        dispatch(addComment(data))
        reset()
    };


    return (
        <>
            <Typography gutterBottom variant='h5' component='p'>Give feedback</Typography>
            <Box
                component='form'
                onSubmit={handleSubmit(onSubmit)}
                sx={{ verticalAlign: 'middle' }}
            >
                <Rating
                    precision={1}
                    name="simple-controlled"
                    value={rating}
                    onChange={(event, newValue) => {
                        setRating(newValue);
                    }}
                />
                <Controller
                    name="text"
                    control={control}
                    rules={{
                        maxLength: { value: 400, message: 'Max length is 400' },
                        minLength: { value: 1, message: 'Min length is 1' }
                    }}
                    render={({ field }: any) => (
                        <TextField
                            {...field}
                            error={!!errors.text}
                            helperText={errors.text ? errors.text?.message : ''}
                            id="outlined-multiline-flexible"
                            label="Feedback"
                            fullWidth
                            multiline
                            maxRows={10}
                        />
                    )}
                />

                <Button sx={{ mt: 2 }} type='submit' variant='contained'>Submit</Button>
            </Box>
        </>
    )
}