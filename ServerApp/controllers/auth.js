const UserService = require('../services/user')

const apiResponse = require('../utils/apiResponse')
const { signToken } = require('../utils/token')

const Status = require('../constants/status')
const Message = require('../constants/message')

const login = (req, res) => {
  req.user.password = undefined

  const accessToken = signToken({
    uid: req._id,
    user_type: req.user_type,
  })

  return res
    .status(Status.OK)
    .json(apiResponse(true, Status.OK, Message.LOGIN_SUCCESS, { user: req.user, accessToken }))
}

const register = async (req, res) => {
  try {
    const user = await UserService.createUser(req.body)
    user.password = undefined

    const accessToken = signToken({
      uid: user._id,
      user_type: user.user_type,
    })

    return res.status(Status.CREATED).json(
      apiResponse(Status.CREATED, Message.OK, {
        user,
        accessToken,
      })
    )
  } catch (error) {
    console.error(error)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

const logout = (req, res) => {}

const changePassword = (req, res) => {}

const forgotPassword = (req, res) => {}

module.exports = {
  login,
  register,
  logout,
  changePassword,
  forgotPassword,
}
