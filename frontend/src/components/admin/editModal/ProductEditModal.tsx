import React, { useEffect } from 'react';
import { Box, Button, Modal, Alert, Stack, TextField, InputAdornment } from '@mui/material';
import { Scooter } from '../../../redux/scootersCatalogReducer';
import { useForm, Controller, SubmitHandler } from "react-hook-form"
import { z } from 'zod'
import { zodResolver } from "@hookform/resolvers/zod";
import { useAppDispatch, useAppSelector } from '../../../redux/hooks';
import LoadingButton from '@mui/lab/LoadingButton';
import CurrencyRubleIcon from '@mui/icons-material/CurrencyRuble';
import BatteryChargingFullIcon from '@mui/icons-material/BatteryChargingFull';
import BoltIcon from '@mui/icons-material/Bolt';
import SpeedIcon from '@mui/icons-material/Speed';
import TimerOutlinedIcon from '@mui/icons-material/TimerOutlined';
import { editScooter } from '../../../redux/adminReducer';


type HandleClose = () => void
type ProductEditModalProps = {
    openProductEdit: boolean,
    productData: Scooter,
    handleClose: HandleClose
}

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '90%',
    bgcolor: 'background.paper',
    borderRadius: '5px',
    boxShadow: 24,
    p: 4,
};


const formSchema = z.object({
    name: z.string().min(1, { message: "Обязательное поле" }).max(30, { message: 'max length is 30' }),
    cost: z.number().positive().max(2147483647),
    description: z.string().min(1, { message: "Обязательное поле" }).max(400, { message: 'max length is 400' }),
    batteryCapacity: z.number().positive().max(50),
    power: z.number().positive().max(25),
    speed: z.number().positive().max(100),
    time: z.number().positive().max(30),
})
type EditItemFormData = z.infer<typeof formSchema>

export const ProductEditModal: React.FC<ProductEditModalProps> = ({ openProductEdit, productData, handleClose }) => {
    const dispatch = useAppDispatch()
    const role = useAppSelector(state => state.auth.role)

    const {
        control,
        setValue,
        formState: {
            errors,
        },
        handleSubmit,
    } = useForm<EditItemFormData>({
        defaultValues: {
            name: productData.name,
            cost: +productData.cost,
            description: productData.description,
            batteryCapacity: +productData.batteryCapacity,
            power: +productData.power,
            speed: +productData.speed,
            time: +productData.time
        },
        resolver: zodResolver(formSchema)
    });

    const onSubmit: SubmitHandler<EditItemFormData> = (data) => {
        dispatch(editScooter({ id: productData.id, ...data }))
        handleClose()
    }

    useEffect(() => {
        if (productData) {
            setValue('name', productData.name);
            setValue('cost', +productData.cost);
            setValue('description', productData.description ? productData.description : '');
            setValue('batteryCapacity', +productData.batteryCapacity);
            setValue('power', +productData.power);
            setValue('speed', +productData.speed);
            setValue('time', +productData.time);
        }
    }, [productData, setValue]);

    return (
        <Modal
            open={openProductEdit}
            onClose={handleClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={style} component="form" onSubmit={handleSubmit(onSubmit)}>
                <Stack direction={{ xs: 'column', md: 'row' }} spacing={4}>
                    <Stack sx={{ width: '100%' }}>
                        <Controller
                            name='name'
                            control={control}
                            render={({ field }) => <TextField {...field}
                                type="text"
                                error={!!errors.name}
                                helperText={errors.name ? errors.name?.message : ''}
                                label="Item name" variant='outlined'
                                fullWidth
                                margin='dense' />}
                        />
                        <Controller
                            name='cost'
                            control={control}
                            render={({ field }) => <TextField {...field}
                                InputProps={{
                                    startAdornment: <InputAdornment position="start"><CurrencyRubleIcon /></InputAdornment>,
                                }}
                                onChange={(event) => field.onChange(+event.target.value)}
                                type="number"
                                error={!!errors.cost}
                                helperText={errors.cost ? errors.cost?.message : ''}
                                label="Cost of item" variant='outlined'
                                fullWidth
                                margin='dense' />}
                        />
                        <Controller
                            name='description'
                            control={control}
                            render={({ field }) => <TextField {...field}
                                type="text"
                                error={!!errors.description}
                                helperText={errors.description ? errors.description?.message : ''}
                                label="Item description" variant='outlined'
                                fullWidth
                                margin='dense' />}
                        />
                        <Stack flexDirection='row' justifyContent='space-around' flexWrap='wrap'>
                            <Controller
                                name='batteryCapacity'
                                control={control}
                                render={({ field }) => <TextField {...field}
                                    InputProps={{
                                        startAdornment: <InputAdornment position="start"><BatteryChargingFullIcon /></InputAdornment>,
                                    }}
                                    onChange={(event) => field.onChange(+event.target.value)}
                                    type="number"
                                    error={!!errors.batteryCapacity}
                                    helperText={errors.batteryCapacity ? errors.batteryCapacity?.message : ''}
                                    label="Battery capacity" variant='outlined'
                                    margin='dense' />}
                            />
                            <Controller
                                name='power'
                                control={control}
                                render={({ field }) => <TextField {...field}
                                    InputProps={{
                                        startAdornment: <InputAdornment position="start"><BoltIcon /></InputAdornment>,
                                    }}
                                    onChange={(event) => field.onChange(+event.target.value)}
                                    type="number"
                                    error={!!errors.power}
                                    helperText={errors.power ? errors.power?.message : ''}
                                    label="Power" variant='outlined'
                                    margin='dense' />}
                            />
                            <Controller
                                name='speed'
                                control={control}
                                render={({ field }) => <TextField {...field}
                                    InputProps={{
                                        startAdornment: <InputAdornment position="start"><SpeedIcon /></InputAdornment>,
                                    }}
                                    onChange={(event) => field.onChange(+event.target.value)}
                                    type="number"
                                    error={!!errors.speed}
                                    helperText={errors.speed ? errors.speed?.message : ''}
                                    label="Speed" variant='outlined'
                                    margin='dense' />}
                            />
                            <Controller
                                name='time'
                                control={control}
                                render={({ field }) => <TextField {...field}
                                    InputProps={{
                                        startAdornment: <InputAdornment position="start"><TimerOutlinedIcon /></InputAdornment>,
                                    }}
                                    onChange={(event) => field.onChange(+event.target.value)}
                                    type="number"
                                    error={!!errors.time}
                                    helperText={errors.time ? errors.time?.message : ''}
                                    label="Time" variant='outlined'
                                    margin='dense' />}
                            />

                        </Stack>
                        <Stack spacing={2} direction='row' justifyContent='flex-end' sx={{ mt: 2 }} >
                            <LoadingButton loading={false} type='submit' variant='contained' >Save</LoadingButton>
                            <Button onClick={() => { handleClose() }} variant='outlined' >Cancel</Button>
                        </Stack>
                    </Stack>
                </Stack>
                {role !== 'ROLE_ADMIN' &&

                    <Alert sx={{ mt: 3 }} variant="outlined" severity="warning">
                        Log in as an administrator, otherwise it won't work
                    </Alert>
                }
            </Box>
        </Modal>
    );
}