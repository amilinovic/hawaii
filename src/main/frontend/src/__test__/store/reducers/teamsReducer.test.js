import teamsReducer from '../../../store/reducers/teamsReducer';
import { RECEIVE_TEAMS } from '../../../store/actions/teamsActions';

describe('Teams reducer', () => {
  const mockTeamsInformation = {
    name: 'TestTeam',
    mails: 'test@mail.com'
  };

  it('should have initial state', () => {
    const initialState = teamsReducer(undefined, []);
    expect(initialState).toEqual([]);
  });

  it('should update teams data with api response', () => {
    const action = {
      type: RECEIVE_TEAMS,
      payload: mockTeamsInformation
    };

    const state = [mockTeamsInformation];

    expect(teamsReducer(undefined, action)).toEqual(state);
  });
});
