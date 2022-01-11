const AttendanceService = require('../services/attendance')

const apiResponse = require('../utils/apiResponse')

const Status = require('../constants/status')
const Message = require('../constants/message')

const getAttendance = async (req, res) => {
  try {
    const attendance = await AttendanceService.getAttendance()
    return res.status(Status.OK).json(apiResponse(Status.OK, Message.GET_ATTENDANCE_SUCCESS, attendance))
  } catch (err) {
    console.error(err.message)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

const getAttendanceById = async (req, res) => {
  try {
    const attendance = await AttendanceService.GetAttendanceById(req.params.id)
    if (!attendance) return res.status(Status.NOT_FOUND).json(apiResponse(Status.NOT_FOUND, Message.NOT_FOUND))
    return res.status(Status.OK).json(apiResponse(Status.OK, Message.GET_ATTENDANCE_BY_ID_SUCCESS, attendance))
  } catch (err) {
    console.error(err.message)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

module.exports = {
  getAttendance,
  getAttendanceById,
}
