import {
  requestApiData,
  receiveApiData
} from '../../../store/actions/EmployeesActions';

describe('employees actions', () => {
  it('should request data from api', () => {
    const action = requestApiData();
    expect(action).toEqual({
      type: 'REQUEST_API_DATA'
    });
  });

  it('should receive data from api', () => {
    const employeeInformation = {};
    const action = receiveApiData(employeeInformation);
    expect(action).toEqual({
      type: 'RECEIVE_API_DATA',
      employeeInformation: {
        ...employeeInformation
      }
    });
  });
});
