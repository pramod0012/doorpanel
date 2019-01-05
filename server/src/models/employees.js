const DbModel = require('./common/dbModel');


class Employees extends DbModel {
    constructor() {
        super('employee');
    }

    async create(employee) {
        const isEmployeeValid = employee
            && Object.prototype.hasOwnProperty.call(employee, 'name')
            && Object.prototype.hasOwnProperty.call(employee, 'age')
            && Object.prototype.hasOwnProperty.call(employee, 'email');
        if (isEmployeeValid) {
            employee.id = await this._generate_id();

            return await this._insert(employee);
        }

        throw new Error('Employee is invalid');
    }

    async changeStatus(status, id) {
        await this._updateById(id, {status})
    }

    async changeRoom(room, id) {
        await this._updateById(id, {room})
    }

}

module.exports = Employees;