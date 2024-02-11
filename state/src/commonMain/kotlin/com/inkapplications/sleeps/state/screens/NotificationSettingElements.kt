package com.inkapplications.sleeps.state.screens

import com.inkapplications.sleeps.state.notifications.NotificationController
import com.inkapplications.sleeps.state.notifications.NotificationsState
import ink.ui.structures.GroupingStyle
import ink.ui.structures.TextStyle
import ink.ui.structures.elements.*
import kotlin.time.Duration

internal object NotificationSettingElements {
    fun create(
        state: NotificationsState,
        notificationController: NotificationController,
    ): Array<UiElement> {
        return when (state) {
            is NotificationsState.Configured -> arrayOf(
                ElementList(
                    items = createWakeAlarmSettings(state, notificationController),
                    groupingStyle = GroupingStyle.Unified,
                ),
                ElementList(
                    items = createSleepAlarmSettings(state, notificationController),
                    groupingStyle = GroupingStyle.Unified,
                ),
            )
            NotificationsState.Initial -> emptyArray()
        }
    }

    private fun createWakeAlarmSettings(
        state: NotificationsState.Configured,
        notificationController: NotificationController,
    ): List<UiElement> {
        val alarmHeading = TextElement(
            text = "Sunrise Alarm",
            style = TextStyle.H3,
        )
        val alarm = createToggleRow(
            text = "Enable",
            checked = state.wakeAlarm,
            onClick = notificationController::onWakeAlarmClick,
        )
        val wakeMargin = MenuRowElement(
            text = "Margin (hours)",
            rightElement = SpinnerElement(
                value = state.alarmMargin.format(),
                hasPreviousValue = state.alarmMargin.isPositive(),
                onNextValue = notificationController::onIncreaseWakeAlarmMargin,
                onPreviousValue = notificationController::onDecreaseWakeAlarmMargin.takeIf { state.alarmMargin.isPositive() } ?: {},
            )
        )

        return listOf(
            alarmHeading,
            alarm,
            wakeMargin,
        )
    }

    private fun Duration.format() = (inWholeMinutes / 60f).toString()

    private fun createSleepAlarmSettings(
        state: NotificationsState.Configured,
        notificationController: NotificationController,
    ): List<UiElement> {

        val sleepHeading = TextElement(
            text = "Sleep Alarm",
            style = TextStyle.H3,
        )
        val sleepNotifications = createToggleRow(
            text = "Enable",
            checked = state.sleepNotifications,
            onClick = notificationController::onSleepNotificationClick,
        )
        val sleepTarget = MenuRowElement(
            text = "Sleep Target (hours)",
            rightElement = SpinnerElement(
                value = state.sleepTarget.format(),
                hasPreviousValue = state.sleepTarget.isPositive(),
                onNextValue = notificationController::onIncreaseSleepTarget,
                onPreviousValue = notificationController::onDecreaseSleepTarget.takeIf { state.sleepTarget.isPositive() } ?: {},
            )
        )
        val sleepMargin = MenuRowElement(
            text = "Margin (hours)",
            rightElement = SpinnerElement(
                value = state.sleepMargin.format(),
                hasPreviousValue = state.sleepMargin.isPositive(),
                onNextValue = notificationController::onIncreaseSleepAlarmMargin,
                onPreviousValue = notificationController::onDecreaseSleepAlarmMargin.takeIf { state.sleepMargin.isPositive() } ?: {},
            )
        )

        return listOf(
            sleepHeading,
            sleepNotifications,
            sleepTarget,
            sleepMargin,
        )
    }

    private fun createToggleRow(
        text: String,
        checked: Boolean,
        onClick: () -> Unit,
    ): MenuRowElement {
        return MenuRowElement(
            text = text,
            onClick = onClick,
            rightElement = CheckBoxElement(
                checked = checked,
                onClick = onClick,
            )
        )
    }

}