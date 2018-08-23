import { getTokenFromSessionStorage } from '../../../store/services/getTokenFromSessionStorage';

describe('getTokenAndRoleFromSessionStorageSaga', () => {
  const sessionStorageMock = {
    getItem(key) {
      return this[key] || null;
    },
    setItem(key, value) {
      this[key] = value;
    }
  };
  beforeEach(() => {
    global.sessionStorage = sessionStorageMock;
  });
  afterEach(() => {
    global.sessionStorage = undefined;
  });

  it('should get token and role if token and role exists', () => {
    global.sessionStorage.setItem('token', '123456');
    global.sessionStorage.setItem('role', 'testRole');
    expect(getTokenFromSessionStorage()).toEqual({
      token: global.sessionStorage.token,
      role: global.sessionStorage.role
    });
  });

  it("should return null if role doesn't exist", () => {
    global.sessionStorage.setItem('token', '123456');
    global.sessionStorage.setItem('role', null);
    expect(getTokenFromSessionStorage()).toEqual(null);
  });

  it("should return null if token doesn't exist", () => {
    global.sessionStorage.setItem('token', null);
    global.sessionStorage.setItem('role', 'testRole');
    expect(getTokenFromSessionStorage()).toEqual(null);
  });
});
