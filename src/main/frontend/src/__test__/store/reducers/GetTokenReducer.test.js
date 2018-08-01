import getTokenReducer from '../../../store/reducers/GetTokenReducer';
import { RECEIVE_TOKEN } from '../../../store/actions/GetTokenActions';

describe('INITIAL_STATE', () => {
  it('is correct', () => {
    const action = { type: 'neka_akcija' };
    const initialState = {};

    expect(getTokenReducer(undefined, action)).toEqual(initialState);
  });

  describe('GET_TOKEN', () => {
    it('returns the correct state', () => {
      const action = { type: RECEIVE_TOKEN, payload: 1 };
      const expectedState = 1;

      expect(getTokenReducer(undefined, action)).toEqual(expectedState);
    });
  });
});
