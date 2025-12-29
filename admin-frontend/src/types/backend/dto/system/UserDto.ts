export interface UserDto {
  id: string
  username: string
  nickname: string
  type: 'super_admin' | 'member'
  status: 'normal' | 'locked' | 'disabled' | 'expired'
}
