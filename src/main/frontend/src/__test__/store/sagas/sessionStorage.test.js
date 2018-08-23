import { getTokenFromSessionStorage } from '../../../store/services/getTokenFromSessionStorage';

describe('getTokenAndRoleFromSessionStorageSaga', () => {
  it('should get token and role if token and role exists', () => {
    const SessionStorageMock = {
      getItem(key) {
        return this[key] || null;
      },

      setItem(key, value) {
        this[key] = value;
      }
    };

    global.sessionStorage = Object.assign({}, SessionStorageMock);

    global.sessionStorage.setItem('token', '123456');
    global.sessionStorage.setItem('role', 'testRole');

    expect(getTokenFromSessionStorage()).toEqual({
      token: global.sessionStorage.token,
      role: global.sessionStorage.role
    });
  });

  it("should return null if role don't exists", () => {
    const SessionStorageMock = {
      getItem(key) {
        return this[key] || null;
      },

      setItem(key, value) {
        this[key] = value;
      }
    };

    global.sessionStorage = Object.assign({}, SessionStorageMock);

    global.sessionStorage.setItem('token', '123456');

    expect(getTokenFromSessionStorage()).toEqual(null);
  });

  it("should return null if token don't exists", () => {
    const SessionStorageMock = {
      getItem(key) {
        return this[key] || null;
      },

      setItem(key, value) {
        this[key] = value;
      }
    };

    global.sessionStorage = Object.assign({}, SessionStorageMock);

    global.sessionStorage.setItem('role', 'testRole');

    expect(getTokenFromSessionStorage()).toEqual(null);
  });
});
