import { getEmployee, getFetching } from '../../store/Selectors';

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
        employee: ''
      };
      expect(getFetching(state)).toEqual();
    });
  });
});
