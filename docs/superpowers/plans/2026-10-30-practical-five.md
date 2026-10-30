# Practical Five implementation plan

**Goal:** Complete Chinese Municipalities with Java, XML Views and incremental commits/pushes.
**Architecture:** MainActivity validates keyboard input through a plain TextProcessor and explicitly opens Municipality. Municipality revalidates the shared extra, selects resources and opens its official URL with ACTION_VIEW.
**Tech stack:** Existing Android Studio scaffold, AGP 9.4.1, Gradle 9.6, Java 11 source, SDK 37/minimum 24, JUnit 4 and AndroidX instrumentation.
**Spec:** Practical 5.pdf and Practical 5 - SubmissionGuidelines.pdf supplied by the student; their copies remain local.

## Constraints and decisions
- Keep the existing project, student work, package com.example.practicalfive and public repository 20243006950-Practical5.
- Array order follows explicit prose: Beijing, Shanghai, Chongqing, Tianjin. The sample XML reverses the final two entries.
- Reuse supplied images and app_logo.png. Use named resources for all UI text and colours.
- Each numbered feature is built/checked, committed and pushed before the next starts. Preserve the initial commit; never rewrite history.
- Invalid/missing detail extras finish safely. Invalid main input shows an EditText error without navigating.
- Keep the Visit site button outside the summary ScrollView. Use dark foreground on bright orange. Use a dark summary panel for orange text to preserve contrast.

## Stages
- [x] 1. Student generated and pushed the initial Empty Views Activity scaffold (f758cd0).
- [x] 2. Complete branding, five images, adaptive/legacy launcher icons and named colours; assemble the app.
- [x] 3. Add four original 80-120-word summaries and primary-source notes; compile resources and check order/counts.
- [ ] 4. Write processor tests, observe failing assertions, implement null/blank/case/whitespace validation and run JUnit.
- [ ] 5. Add accessible main-screen XML with single-line Go input; assemble.
- [ ] 6. Write navigation/error instrumentation tests, observe failures, implement explicit Intent and registered Municipality with safe extra handling; run tests.
- [ ] 7. Add tests for all four resource combinations, implement dynamic image/summary/colour selection and run device tests.
- [ ] 8. Add website/no-browser tests, implement city-specific ACTION_VIEW with ActivityNotFoundException handling and run tests.
- [ ] 9. Run build, lint, complete unit/instrumentation suite, review code independently, capture screenshots and document reproducible results.

## Review focus
- Null, wrong-type, blank and unsupported extras must not crash or display a mismatched city.
- Chongqing/Tianjin summaries must use indices 2/3 respectively.
- Soft IME Go and physical Enter must trigger one navigation.
- No browser must leave the detail usable and show feedback.
- Small screens, landscape, large fonts and system insets must leave the Visit site button reachable.
