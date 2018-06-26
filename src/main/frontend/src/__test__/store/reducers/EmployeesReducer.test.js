import employeesReducer from '../../../store/reducers/EmployeesReducer';
import {
  requestApiData,
  receiveApiData
} from '../../../store/actions/EmployeesActions';

describe('employees reducer', () => {
  const mockEmployeesInformation = [
    {
      name: 'someuser',
      url: 'https://randomuser.me/api/'
    }
  ];

  it('should set default state', () => {
    const state = employeesReducer(undefined, { type: '@@INIT' });
    expect(state).toEqual({ fetching: '' });
  });

  it('should set new state', () => {
    const stateWithResults = {
      type: 'RECEIVE_API_DATA',
      employee: mockEmployeesInformation
    };

    const requestAction = requestApiData();
    const newState = employeesReducer(stateWithResults, requestAction);

    expect(newState.employee).toBe(mockEmployeesInformation);
  });
});
