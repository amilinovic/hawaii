import { getEmployee } from '../../store/Selectors';

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
