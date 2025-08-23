# OAuth2 Setup Guide

This guide explains how to configure OAuth2 authentication for the NIMAS2PDF application.

## Overview

The application supports OAuth2 authentication with multiple providers:
- **Google** (recommended for general use)
- **GitHub** (good for developer-focused environments)
- **Microsoft/Azure** (good for enterprise environments)

## Setting Up OAuth2 Providers

### 1. Google OAuth2 Setup

1. **Go to Google Cloud Console**: https://console.cloud.google.com/
2. **Create a new project** or select an existing one
3. **Enable the Google+ API**:
   - Go to "APIs & Services" > "Library"
   - Search for "Google+ API" and enable it
4. **Create OAuth2 credentials**:
   - Go to "APIs & Services" > "Credentials"
   - Click "Create Credentials" > "OAuth 2.0 Client IDs"
   - Choose "Web application"
   - Add authorized redirect URIs:
     - `http://localhost:8080/login/oauth2/code/google` (for development)
     - `https://yourdomain.com/login/oauth2/code/google` (for production)
5. **Copy the Client ID and Client Secret**

### 2. GitHub OAuth2 Setup

1. **Go to GitHub Settings**: https://github.com/settings/developers
2. **Create a new OAuth App**:
   - Application name: "NIMAS2PDF"
   - Homepage URL: `http://localhost:3000` (for development)
   - Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
3. **Copy the Client ID and Client Secret**

### 3. Microsoft/Azure OAuth2 Setup

1. **Go to Azure Portal**: https://portal.azure.com/
2. **Go to Azure Active Directory** > "App registrations"
3. **Create a new registration**:
   - Name: "NIMAS2PDF"
   - Supported account types: "Accounts in any organizational directory and personal Microsoft accounts"
   - Redirect URI: `http://localhost:8080/login/oauth2/code/microsoft`
4. **Copy the Application (client) ID**
5. **Create a client secret**:
   - Go to "Certificates & secrets"
   - Create a new client secret
   - Copy the secret value

## Configuration

### Environment Variables

Set the following environment variables:

```bash
# Google OAuth2
export GOOGLE_CLIENT_ID="your-google-client-id"
export GOOGLE_CLIENT_SECRET="your-google-client-secret"

# GitHub OAuth2
export GITHUB_CLIENT_ID="your-github-client-id"
export GITHUB_CLIENT_SECRET="your-github-client-secret"

# Microsoft OAuth2
export MICROSOFT_CLIENT_ID="your-microsoft-client-id"
export MICROSOFT_CLIENT_SECRET="your-microsoft-client-secret"
```

### Docker Compose Environment

For Docker deployment, add these to your `docker-compose.yml`:

```yaml
services:
  nimas2pdf-web:
    environment:
      - GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
      - GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
      - GITHUB_CLIENT_ID=${GITHUB_CLIENT_ID}
      - GITHUB_CLIENT_SECRET=${GITHUB_CLIENT_SECRET}
      - MICROSOFT_CLIENT_ID=${MICROSOFT_CLIENT_ID}
      - MICROSOFT_CLIENT_SECRET=${MICROSOFT_CLIENT_SECRET}
```

### .env File (for Docker Compose)

Copy the example environment file and configure your credentials:

```bash
cp env.example .env
```

Then edit `.env` with your OAuth2 credentials:

```env
# OAuth2 Configuration
GOOGLE_CLIENT_ID=your-actual-google-client-id
GOOGLE_CLIENT_SECRET=your-actual-google-client-secret
GITHUB_CLIENT_ID=your-actual-github-client-id
GITHUB_CLIENT_SECRET=your-actual-github-client-secret
MICROSOFT_CLIENT_ID=your-actual-microsoft-client-id
MICROSOFT_CLIENT_SECRET=your-actual-microsoft-client-secret
```

**Important**: The `.env` file is automatically excluded from version control to protect your credentials.

## Testing the Setup

1. **Start the application**
2. **Navigate to** `http://localhost:3000`
3. **Click "Login"** - you should see login options for configured providers
4. **Test each provider** to ensure authentication works

## User Roles

The application supports three user roles:

- **USER**: Can upload packages and start conversions
- **ADMIN**: Can manage users and system settings
- **VIEWER**: Can only view packages and jobs (read-only)

### First User

The first user to register automatically becomes an **ADMIN**.

### Managing User Roles

Admins can manage user roles through the admin interface (to be implemented).

## Security Considerations

1. **Keep secrets secure**: Never commit OAuth2 client secrets to version control
2. **Use HTTPS in production**: Configure proper SSL/TLS for production deployments
3. **Validate redirect URIs**: Ensure redirect URIs match exactly in OAuth2 provider settings
4. **Regular rotation**: Rotate OAuth2 client secrets regularly

## Troubleshooting

### Common Issues

1. **"Invalid redirect URI"**:
   - Check that redirect URIs in OAuth2 provider settings match exactly
   - Ensure no trailing slashes or extra parameters

2. **"Access denied"**:
   - Check OAuth2 scopes are correctly configured
   - Ensure the provider account has necessary permissions

3. **"Session expired"**:
   - Check session timeout configuration
   - Ensure cookies are properly configured

### Debug Logs

Enable debug logging for OAuth2 by setting:

```properties
logging.level.org.springframework.security.oauth2=DEBUG
logging.level.org.springframework.security.web=DEBUG
```

## Next Steps

1. **Configure your OAuth2 providers** using the instructions above
2. **Set environment variables** with your client credentials
3. **Test the authentication flow**
4. **Configure user roles** as needed for your organization
