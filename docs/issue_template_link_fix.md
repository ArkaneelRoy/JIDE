# Issue Template Link Fix

## Overview

This document details the correction of malformed GitHub repository links within the issue templates (`BUG.yml` and `FEATURE.yml`) of the AndroidIDE Ultra project. These links previously pointed to the `AndroidIDE UltraOfficial/AndroidIDE Ultra` repository, which is incorrect for this fork. The links have been updated to correctly reference the `Willow7737/AndroidIDE-Ultra` repository.

## Rationale

The original issue templates contained a link to the Code of Conduct that used the former repository name. This caused broken links for users attempting to access the Code of Conduct via the issue submission forms. Correcting these links ensures that contributors are directed to the appropriate document within the current repository, improving the user experience and maintaining consistency in project documentation.

## Changes Made

The following files were modified:

- `.github/ISSUE_TEMPLATE/BUG.yml`
- `.github/ISSUE_TEMPLATE/FEATURE.yml`

In both files, the URL for the Code of Conduct was updated from:

`https://github.com/AndroidIDE UltraOfficial/AndroidIDE Ultra/blob/dev/CODE_OF_CONDUCT.md`

to:

`https://github.com/Willow7737/AndroidIDE-Ultra/blob/dev/CODE_OF_CONDUCT.md`

This ensures that all references within the issue templates correctly point to the Code of Conduct in the `Willow7737/AndroidIDE-Ultra` repository.
