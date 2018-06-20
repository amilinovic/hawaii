import styled from 'styled-components';
import { prop } from 'styled-tools';

// TODO change mocked image data with actual data

export const UserImage = styled.div`
  border-radius: 50%;
  width: ${prop('size', '50px')};
  height: ${prop('size', '50px')};
  overflow: hidden;
  display: inline-flex;
  justify-content: center;
  background-image: url('${prop(
    'image',
    'https://openclipart.org/download/247324/abstract-user-flat-1.svg'
  )}');
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
  margin-right: 15px;
`;
