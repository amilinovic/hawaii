import {
  getEmployee,
  getFetching,
  getAuthorization
} from '../../store/Selectors';

describe('Employee Selector', () => {
  describe('Get Employee Selector', () => {
    it('should return state of employee', () => {
      const state = {
        employee: {
          name: 'John',
          surname: 'Doe'
        }
      };
      expect(getEmployee(state)).toEqual({ name: 'John', surname: 'Doe' });
    });
  });
  describe('Fetch Employee Selector', () => {
    it('should return fetch state of employee', () => {
      const state = {
        employee: {
          fetching: 123
        }
      };
      expect(getFetching(state)).toEqual(123);
    });
  });
});

describe('Get Authorization Selector', () => {
  it('should return authorization', () => {
    const state = {
      authorization: {
        token: '2d153236-d103-4fa0-a3cb-8f26a14c1c45',
        role: 'HR_MANAGER'
      }
    };
    expect(getAuthorization(state)).toEqual({
      token: '2d153236-d103-4fa0-a3cb-8f26a14c1c45',
      role: 'HR_MANAGER'
    });
  });
});
