import employeesReducer from '../../../store/reducers/EmployeesReducer';
import { requestApiData } from '../../../store/actions/EmployeesActions';

describe('employees reducer', () => {
  const mockEmployeesInformation = [
    {
      name: 'someuser',
      url: 'https://randomuser.me/api/'
    }
  ];

  it('should have default state', () => {
    const state = employeesReducer(undefined, {});
    expect(state).toEqual({ fetching: '', error: null });
  });

  it('should update employee data with api response', () => {
    const stateWithResults = {
      type: 'RECEIVE_API_DATA',
      employee: mockEmployeesInformation
    };

    const requestAction = requestApiData();
    const newState = employeesReducer(stateWithResults, requestAction);

    expect(newState.employee).toBe(mockEmployeesInformation);
  });
});
