const DbModel = require('./common/dbModel');


class Workers extends DbModel {
    constructor() {
        super('worker');
    }

    async create(worker) {
        const isWorkerValid = worker
            && Object.prototype.hasOwnProperty.call(worker, 'name')
            && Object.prototype.hasOwnProperty.call(worker, 'password')
            && Object.prototype.hasOwnProperty.call(worker, 'email');
        if (isWorkerValid) {
            worker.id = await this._generate_id();

            return await this._insert(worker);
        }

        throw new Error('Worker is invalid');
    }

    async changeStatus(status, id) {
        await this._updateById(id, {status})
    }

    async changeEmail(email, id) {
        await this._updateById(id, {email})
    }

    async changePhone(phoneNumber, id) {
        await this._updateById(id, {phoneNumber})
    }

    async changeRoom(room, id) {
        await this._updateById(id, {room})
    }

    async addTimeslot(event, id) {
        console.log(id);
        await this._MongooseModel.updateOne({id}, {$addToSet:{timeslots:event}});
    }

    async removeTimeslot(eventId, id) {
        console.log(eventId)
        await this._MongooseModel.updateOne({id}, {$pull:{timeslots:{id:eventId}}});
    }

}

module.exports = Workers;
