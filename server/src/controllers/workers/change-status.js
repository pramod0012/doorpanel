module.exports = async (ctx) => {
    console.log("request received");
	const {status} = ctx.request.body;
    console.log("status change request received");
    const worker = await ctx.req.user;
    await ctx.workerModel.changeStatus(status, worker.id);

    const room = worker.room;
    const tablet = await ctx.tabletModel.getBy({room});
    if (!tablet) {
        console.log(`There is no active tablet at room ${room}`)
    }

    const messageToTablet = {
        data: {subject: 'changeStatus', workerId: worker.id.toString(), status},
        topic: "80b" // Later it will be real room
    };
    ctx.admin.messaging().send(messageToTablet)
        .then((response) => {
            console.log('Successfully sent message:', response);
        })
        .catch((error) => {
            console.log('Error sending message:', error);
        });
    ctx.body = status;
};