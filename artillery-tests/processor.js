module.exports = {
    afterPOSTHandler: afterPOSTHandler
};

function afterPOSTHandler(requestParams, response, context, ee, next) {
    var id = /[^/]*$/.exec(context.vars["location"])[0];
    context.vars["location"] = id;
    return next();
}
