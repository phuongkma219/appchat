module.exports = {
  fromEntity: (entity) => {
    if (entity == null){
      return {}
    }
    console.log(entity)
    return {
      id: entity["_id"],
      dob: entity["dob"] && entity["dob"].getTime(),
      address:  entity["address"],
      phone: entity["phone"],
      name: entity["name"]
    }
  }
}