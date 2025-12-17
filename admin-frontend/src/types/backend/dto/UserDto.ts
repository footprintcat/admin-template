export interface UserDto {
  id: string
  username: string
  nickname: string
  roleId: string
  telephone?: string | undefined // TODO
  status: string // TODO enum
}
