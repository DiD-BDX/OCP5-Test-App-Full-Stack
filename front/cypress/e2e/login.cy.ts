/// <reference types="cypress" />
describe('Login tests', () => {
  it('Login successfull, go to the sessions page', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
  
  it('Login successfull, should receive status 200 OK', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.wait('@session').its('response.statusCode').should('eq', 200)
    })

  it('Login failed with wrong Email', () => {
    cy.visit('/login')

   cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        path: "/api/auth/login",
        error: "Unauthorized",
        message: "Bad credentials",
        status: 401
      },
    })

    cy.get('input[formControlName=email]').type("yoga@studio.fr")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.contains('An error occurred')
  })

  it('Login failed with wrong password', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 404,
      body: {
        path: "/api/auth/login",
        error: "Unauthorized",
        message: "Bad credentials",
        status: 401
      },
    })
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!123"}{enter}{enter}`)
    cy.contains('An error occurred')
  })

  it('Submit button remains disabled until all fields are filled', () => {
    cy.visit('/login')
    // Assurez-vous que le bouton est initialement désactivé
    cy.get('button[type="submit"]').should('be.disabled')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('button[type="submit"]').should('be.disabled')
    cy.get('input[formControlName=password]').type("test!1234")

    // Maintenant que tous les champs sont remplis, le bouton doit être activé
    cy.get('button[type="submit"]').should('not.be.disabled')
  })

  it ('the hide password button should show the password', () => {
    cy.visit('/login')
    cy.get('input[formControlName=password]').type("test!1234")
    cy.get('button[aria-label="Hide password"]').click()
    cy.get('input[formControlName=password]').should('have.attr', 'type', 'text')
  })

  it ('the hide password button should hide the password', () => {
    cy.visit('/login')
    cy.get('input[formControlName=password]').type("test!1234")
    cy.get('button[aria-label="Hide password"]').click()
    cy.get('button[aria-label="Hide password"]').click()
    cy.get('input[formControlName=password]').should('have.attr', 'type', 'password')
  })
});

