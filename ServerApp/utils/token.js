const jwt = require('jsonwebtoken')
const JWT_KEY = process.env.JWT_KEY || 'giaynhapcoder'
const duration = parseInt(process.env.JWT_DURATION || 2400)
const iat = Math.floor(new Date() / 1000)
const exp = iat + duration

const signToken = (data) => {
  const token = jwt.sign(
    {
      header: { alg: 'HS512', typ: 'JWT' },
      payload: { data, iat, exp },
    },
    JWT_KEY
  )
  return token
}

const verifyToken = (token) => {
  return jwt.verify( token,JWT_KEY, { algorithms: ['HS512'] } );
}

module.exports = {
  signToken,
  verifyToken,
}
