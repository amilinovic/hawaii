import styled from 'styled-components';

export const UserImage = styled.div`
  border-radius: 50%;
  width: 50px;
  height: 50px;
  overflow: hidden;
  display: inline-flex;
  justify-content: center;
  background-image: url('${props =>
    props.image
      ? props.image
      : 'https://openclipart.org/download/247324/abstract-user-flat-1.svg'}');
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
  margin-right: 15px;
`;
