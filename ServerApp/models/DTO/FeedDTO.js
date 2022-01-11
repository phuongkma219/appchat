const userDTO = require("./UserDTO")
module.exports = {
  fromEntity: (entity) => {
    if (entity == null){
      return {}
    }
    
    return {
      id: entity["_id"],
      user: userDTO.fromEntity(entity["user"]),
      content:  entity["content"],
      created_at: entity["created_at"] && entity["created_at"].getTime()
    }
  }
}