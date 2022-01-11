const apiResponse = (success, status, message, data) => {
  return {
    code: status,
    message,
    data
  } 
}

module.exports = apiResponse
