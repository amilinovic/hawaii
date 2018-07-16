import {
  requestApiData,
  receiveApiData
} from '../../../store/actions/EmployeesActions';

describe('employees actions creators', () => {
  it('should create request data from api action', () => {
    const action = requestApiData();
    expect(action).toEqual({
      type: 'REQUEST_API_DATA'
    });
  });

  it('should create receive data from api action', () => {
    const employeeInformation = {
      name: 'John',
      lastName: 'Doe'
    };
    const action = receiveApiData(employeeInformation);
    expect(action).toEqual({
      type: 'RECEIVE_API_DATA',
      payload: {
        ...employeeInformation
      }
    });
  });
});
