package com.advmeds.keyboard

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo

open class MyInputMethodService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard

    private var mCurKeyboard: Int = 0
    private var caps: Boolean = false

    override fun onCreateInputView(): View {

        keyboardView = layoutInflater.inflate(R.layout.keyboard, null).let { it as KeyboardView }.also {

            it.setOnKeyboardActionListener(this)
            it.isPreviewEnabled = false
        }

        return keyboardView
    }

    override fun onPress(primaryCode: Int) { keyboardView.isPreviewEnabled = false }

    override fun onRelease(primaryCode: Int) { }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {

        val ic = currentInputConnection

        ic ?: return

//        playSoundEffect(primaryCode)

        when (primaryCode) {

            Keyboard.KEYCODE_DELETE -> {

                ic.deleteSurroundingText(1, 0)
            }
            Keyboard.KEYCODE_CANCEL -> {

                requestHideSelf(0)
            }
            Keyboard.KEYCODE_SHIFT -> {

                caps = !caps
                keyboard.setShifted(caps)
                keyboardView.invalidateAllKeys()
                keyboard.keys.asSequence().first { it.codes.first() == primaryCode }?.icon = resources.getDrawable(if (caps) R.drawable.ic_keyboard_capslock else R.drawable.ic_keyboard_caps)
            }
            0 -> { }
            else -> {

                var code = primaryCode.toChar()

                if (code.isLetter() && caps) { code = code.toUpperCase() }

                ic.commitText(code.toString(), 1)
            }
        }
    }

    private fun playSoundEffect(primaryCode: Int) {

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val effectType = when (primaryCode) {
            Keyboard.KEYCODE_DELETE  ->  AudioManager.FX_KEYPRESS_DELETE
            Keyboard.KEYCODE_CANCEL  ->  AudioManager.FX_KEYPRESS_RETURN
            32                       ->  AudioManager.FX_KEYPRESS_SPACEBAR
            else                     ->  AudioManager.FX_KEYPRESS_STANDARD
        }

        audioManager.playSoundEffect(effectType)
    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        mCurKeyboard = attribute?.inputType ?: 0
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)

        keyboard = when (mCurKeyboard) {
            InputType.TYPE_CLASS_NUMBER,
            InputType.TYPE_CLASS_PHONE                      ->  Keyboard(this, R.xml.key_number)
            InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
            InputType.TYPE_CLASS_TEXT                       ->  Keyboard(this, R.xml.key_id)
            InputType.TYPE_TEXT_VARIATION_PASSWORD,
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD  ->  Keyboard(this, R.xml.key_password)
            else                                            ->  Keyboard(this, R.xml.key_qwerty)
        }

        keyboardView.keyboard = keyboard
    }

    override fun onEvaluateInputViewShown(): Boolean {
        super.onEvaluateInputViewShown()
        return true
    }

    override fun onUpdateExtractingVisibility(ei: EditorInfo?) {
        ei?.imeOptions = ei?.imeOptions ?: EditorInfo.IME_FLAG_NO_EXTRACT_UI
        super.onUpdateExtractingVisibility(ei)
    }

    override fun onText(text: CharSequence?) { }

    override fun swipeRight() { }

    override fun swipeLeft() { }

    override fun swipeUp() { }

    override fun swipeDown() { }
}