import { Stack, Skeleton } from '@mui/material'


const ProductCardSkeleton = () => (
  <Stack sx={{ height: 396.5 }} spacing={1}>
    <Skeleton animation='wave' variant='rounded' width={245} height={175} />
    <Skeleton animation='wave' variant='rounded' width={245} height={64} />
    <Skeleton animation='wave' variant='rounded' width={245} height={88} />
    <Stack flexDirection='row'>
      <Skeleton animation='wave' sx={{ mr: 3 }} variant='rounded' width={110} height={30} />
      <Skeleton animation='wave' variant='rounded' width={110} height={30} />
    </Stack>
  </Stack>
)

export default ProductCardSkeleton

