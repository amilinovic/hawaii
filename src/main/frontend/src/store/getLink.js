import { apiPrefix } from '../env';

export const getLink = (link, prefix = apiPrefix) => {
  return `${prefix}${link}`;
};
