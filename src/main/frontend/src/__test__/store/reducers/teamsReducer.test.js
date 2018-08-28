import teamsReducer from '../../../store/reducers/teamsReducer';
import { requestTeams } from '../../../store/actions/teamsActions';

describe('Teams reducer', () => {
  const mockTeamsInformation = {
    name: 'TestTeam',
    mails: 'test@mail.com'
  };

  it('should have default state', () => {
    const state = teamsReducer(undefined, {});
    expect(state).toEqual({ teams: null });
  });

  it('should update teams data with api response', () => {
    const stateWithResults = {
      type: 'RECEIVE_TEAMS',
      teams: mockTeamsInformation
    };

    const requestAction = requestTeams();
    const newState = teamsReducer(stateWithResults, requestAction);

    expect(newState.teams).toBe(mockTeamsInformation);
  });
});
