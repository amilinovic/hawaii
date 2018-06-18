import { injectGlobal } from 'styled-components';

injectGlobal`
  @import url('https://fonts.googleapis.com/css?family=Montserrat');

*{
  user-select: none;	
}	
	
body {	
  font-family: 'Montserrat', sans-serif;
  -webkit-font-smoothing: antialiased;	
}

#root{
  display: flex;
  min-height: 100vh;
  flex-direction: column;
}

a {	
  text-decoration: none;	
  &:hover,	
  &:focus,	
  &:active {	
    text-decoration: none;	
  }	
}	
	
button {	
  border: none;	
  &:hover,	
  &:focus,	
  &:active {	
    outline: none;	
  }	
}	
	
img {	
  max-width: 100%;	
}
	
textarea {	
  resize: none;	
  overflow-y: hidden;	
  &:focus,	
  &:active {	
    outline: none;	
  }	
}	
`;
