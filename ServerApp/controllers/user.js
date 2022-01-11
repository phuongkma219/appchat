const ACTUser = require('../models/actUser')
const UserSubjectRoom = require('../models/userSubjectRoom')
const UserInfo = require('../models/userInfo')
const UserPoint = require('../models/userPoint')
const UserAvgPoint = require('../models/userAvgPoint')

const getUserAct = async (req, res) => {
  try {
    const user = await ACTUser.find().sort({ date: -1 })
    res.json(user)
  } catch (err) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

const getUseActById = async (req, res) => {
  try {
    const user = await ACTUser.findOne({
      user: req.id,
    })
    if (!user) return res.status(400).json({ msg: 'not found' })
    res.json(user)
  } catch (err) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

const getUserSubjectRoom = async (req, res) => {
  try {
    const user = await UserSubjectRoom.find().sort({ data: -1 })
    res.json(user)
  } catch (e) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

const getUserSubjectRoomById = async (req, res) => {
  try {
    const user = await UserSubjectRoom.findOne({
      user: req.id,
    })
    if (!user) return res.status(400).json({ msg: 'not found' })
    res.json(user)
  } catch (err) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

const getUserInfo = async (req, res) => {
  try {
    const user = await UserInfo.find().sort({ data: -1 })
    res.json(user)
  } catch (e) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

const getUserInfoById = async (req, res) => {
  try {
    const user = await UserInfo.findOne({
      user: req.id,
    })
    if (!user) return res.status(400).json({ msg: 'not found' })
    res.json(user)
  } catch (err) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

const getUserPoint = async (req, res) => {
  try {
    const user = await UserPoint.find().sort({ data: -1 })
    res.json(user)
  } catch (e) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

const getUserPointById = async (req, res) => {
  try {
    const user = await UserPoint.findOne({
      user: req.id,
    })
    if (!user) return res.status(400).json({ msg: 'not found' })
    res.json(user)
  } catch (err) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

const getUserAvgPoint = async (req, res) => {
  try {
    const user = await UserAvgPoint.find().sort({ data: -1 })
    res.json(user)
  } catch (e) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

const getUserAvgPointById = async (req, res) => {
  try {
    const user = await UserAvgPoint.findOne({
      user: req.id,
    })
    if (!user) return res.status(400).json({ msg: 'not found' })
    res.json(user)
  } catch (err) {
    console.error(err.message)
    res.status(500).send('Server Error')
  }
}

module.exports = {
  getUserAct,
  getUserSubjectRoom,
  getUserInfo,
  getUserPoint,
  getUserAvgPoint,
  getUseActById,
  getUserSubjectRoomById,
  getUserInfoById,
  getUserPointById,
  getUserAvgPointById,
}
