module.exports = {
    setHouseID: setHouseID,
    setRentalID: setRentalID
};

function setHouseID(requestParams, response, context, ee, next) {
    var id = /[^/]*$/.exec(context.vars["houseID"])[0];
    context.vars["houseID"] = id;
    return next();
}

function setRentalID(requestParams, response, context, ee, next) {
    var id = /[^/]*$/.exec(context.vars["rentalID"])[0];
    context.vars["rentalID"] = id;
    return next();
}
