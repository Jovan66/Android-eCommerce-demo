package com.j_and_a.demo_catalogo_eccomerce.utilities

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URI
import java.util.ArrayList
import kotlin.math.roundToInt

/**
 * This class was hand written by Me and Gabriele
 * has tons of utilities to programmatically handle graphics and customization
 * but it needs a strong update and SKD profing
 * TODO update the whole class
 * **/

class Graphics {

    companion object {
        fun lockUserInteraction(context: Activity) {
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }

        fun unlockUserInteraction(context: Activity) {
            context.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        fun isUserInteractionLocked(context: Activity): Boolean{
            val attrs: WindowManager.LayoutParams = context.window.attributes
            return attrs.flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE != 0
        }

        fun hideSoftKeyboard(activity: Activity){
            if (activity.currentFocus != null) {
                val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }

        fun hideActivityBar(activity: Activity) {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }

            val actionBar = activity.actionBar
            actionBar?.hide()
        }

        fun showActivityBar(activity: Activity) {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }

            val actionBar = activity.actionBar
            actionBar?.show()
        }

        fun getActivityFromContext(originalContext: Context?): Activity? {
            var context = originalContext
            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context
                }
                context = context.baseContext
            }
            return null
        }

        /* fun getScreenHeight(activity: Activity?): Int {
            if(activity == null)
                return 0

            val view = activity.window.decorView.rootView

            return view.measuredHeight - getNavigationBarHeight() - getStatusBarHeight()
        }

        fun getScreenWidth(activity: Activity?): Int {
            if(activity == null)
                return 0

            val view = activity.window.decorView.rootView
            return view.measuredWidth
        }

       private fun isProgressBarPresent(mainView: ConstraintLayout): Boolean{
            return mainView.findViewById<ViewGroup>(R.id.progressBarContainer) != null
        }

        fun addProgressBar(mainView: ConstraintLayout) {
            if(isProgressBarPresent(mainView))
                return

            val applicationContext: Context = Moodbe.applicationContext()
            val myInflater = LayoutInflater.from(applicationContext)
            val progressBarLayout = myInflater.inflate(R.layout.progress_bar_layout, null, false)

            val progressBarContainer = progressBarLayout.findViewById<ConstraintLayout>(R.id.progressBarContainer)

            mainView.addView(progressBarLayout)
            centerPopup(mainView, progressBarLayout, mainView, 0, 0, 0, 0)
            progressBarLayout.z = dpToPx(mainView.context, 20).toFloat()

            progressBarContainer.alpha = 0f
            progressBarContainer.animate().alpha(1f).setDuration(200).start()
        }

        fun removeProgressBar(mainView: ConstraintLayout){
            val progressBarContainer = mainView.findViewById<ViewGroup>(R.id.progressBarContainer)
            progressBarContainer?.animate()?.alpha(0f)?.setDuration(200)?.withEndAction{
                Handler(Looper.getMainLooper()).postDelayed({
                    mainView.removeView(progressBarContainer)
                }, 10)
            }?.start() ?: kotlin.run {
                Log.d("RemoveProgressBar", "Error, could not remove progress bar")
            }
        }

        fun adaptToStatusBar(view: View) {
            val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topMargin = layoutParams.topMargin + Moodbe.statusBarHeight
            view.layoutParams = layoutParams
        }

        fun adaptToNavigationBar(view: View) {
            val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.bottomMargin = layoutParams.bottomMargin + Moodbe.navigationBarHeight
            view.layoutParams = layoutParams
        }

        fun getNavigationBarHeight(): Int{
            return Moodbe.navigationBarHeight
        }

        fun getStatusBarHeight(): Int{
            return Moodbe.statusBarHeight
        }*/

        fun getBitmapFromString(
            context: Context,
            string: String?,
            options: BitmapFactory.Options? = null
        ): Bitmap? {
            val imageId = context.resources.getIdentifier(string, "drawable", context.packageName)

            var imageBitmap = BitmapFactory.decodeResource(context.resources, imageId, options)

            if (imageBitmap != null && options == null)
                imageBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true)

            return imageBitmap
        }

        fun getDrawableFromString(context: Context, string: String?): Drawable? {
            val drawableId =
                context.resources.getIdentifier(string, "drawable", context.packageName)

            return if(drawableId > 0)
                ResourcesCompat.getDrawable(context.resources, drawableId, null)
            else
                null
        }

        fun getDrawableIdFromString(context: Context, string: String?): Int{
            return context.resources.getIdentifier(string, "drawable", context.packageName)
        }

        fun colorToBitmap(width: Int, height: Int, color: Int): Bitmap {
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            canvas.drawColor(color)
            return bmp
        }

        fun dpToPx(applicationContext: Context, dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                applicationContext.resources.displayMetrics
            ).toInt()
        }

        fun pxToDp(applicationContext: Context, px: Int): Int {
            return (px / applicationContext.resources.displayMetrics.density).toInt()
        }

        fun addAlphaToColor(color: Int, alpha: Double): Int {
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)
            val alpha = StrictMath.floor(if (alpha >= 1.0) 255.0 else alpha * 256.0)
            return Color.argb(alpha.roundToInt(), red, green, blue)
        }

        /*fun getTypefaceFromStyle(fontStyle: FontStyle?): Typeface? {
            var typeface: Typeface? = null
            val languagesCodes: MutableList<String> =
                ArrayList()
            languagesCodes.add("zh")

            val useAlternativeFont = languagesCodes.contains(Model.instance.localData.languageCode)

            when (fontStyle) {
                FontStyle.Regular -> typeface = if (!useAlternativeFont) ResourcesCompat.getFont(
                    Moodbe.applicationContext(),
                    Moodbe.FONT_REGULAR
                ) else ResourcesCompat.getFont(
                    Moodbe.applicationContext(),
                    Moodbe.FONT_ALTERNATIVE_REGULAR
                )
                FontStyle.Medium -> typeface = if (!useAlternativeFont) ResourcesCompat.getFont(
                    Moodbe.applicationContext(),
                    Moodbe.FONT_MEDIUM
                ) else ResourcesCompat.getFont(
                    Moodbe.applicationContext(),
                    Moodbe.FONT_ALTERNATIVE_MEDIUM
                )
                FontStyle.DemiBold -> typeface = if (!useAlternativeFont) ResourcesCompat.getFont(
                    Moodbe.applicationContext(),
                    Moodbe.FONT_DEMIBOLD
                ) else ResourcesCompat.getFont(
                    Moodbe.applicationContext(),
                    Moodbe.FONT_ALTERNATIVE_DEMIBOLD
                )
                FontStyle.Bold -> typeface = if (!useAlternativeFont) ResourcesCompat.getFont(
                    Moodbe.applicationContext(),
                    Moodbe.FONT_BOLD
                ) else ResourcesCompat.getFont(
                    Moodbe.applicationContext(),
                    Moodbe.FONT_ALTERNATIVE_BOLD
                )
            }
            return typeface
        }

        fun additionalTextSize(textSize: Int, activity: Activity?): Int {
            var height: Int = Graphics.getScreenHeight(activity)
            var width: Int = Graphics.getScreenWidth(activity)
            if (height < width) {
                val temp = width
                width = height
                height = temp
            }
            val proportion = width.toDouble() / height
            val pixelsFrame = width * height / 100000f.toDouble()
            Log.d("proportion", proportion.toString() + "")
            Log.d("pixelsFrame", pixelsFrame.toString() + "")
            if (pixelsFrame <= 22) return textSize

            //double proportionValue = (proportion - originalProportion) / (maxProportion - originalProportion);
            val additionalSize = pixelsFrame * proportion
            Log.d("additionalSize", additionalSize.toString() + "")
            return Math.round(textSize + additionalSize).toInt()
        }*/

        fun bitmapToDrawable(context: Context, bitmap: Bitmap?): Drawable? {
            return BitmapDrawable(context.resources, bitmap)
        }

        fun drawableToBitmap(gradientDrawable: Drawable, width: Int, height: Int): Bitmap? {
            val conf = Bitmap.Config.ARGB_8888
            val bitmap = Bitmap.createBitmap(width, height, conf)
            val canvas = Canvas(bitmap)
            gradientDrawable.draw(canvas)
            return bitmap
        }

        fun drawableToBitmap(drawable: Drawable): Bitmap? {
            return (drawable as BitmapDrawable).bitmap
        }

        fun makeRoundBitmap(originalBitmap: Bitmap, radius: Int): Bitmap? {
            val output = Bitmap.createBitmap(
                originalBitmap.width,
                originalBitmap.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)
            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, output.width, output.height)
            paint.isDither = true
            paint.isFilterBitmap = true
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawCircle(
                output.width / 2.toFloat(),
                output.height / 2.toFloat(),
                radius.toFloat(),
                paint
            )
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(originalBitmap, rect, rect, paint)
            return output
        }

        /*fun roundBitmap(bitmap: Bitmap, cornerRadiusDp: Int): Bitmap {
            return roundBitmapPx(
                bitmap, dpToPx(
                    Moodbe.applicationContext(),
                    cornerRadiusDp
                )
            )
        }*/

        fun roundBitmapPx(bitmap: Bitmap, cornerRadiusPx: Int): Bitmap {
            val output = Bitmap.createBitmap(
                bitmap.width, bitmap
                    .height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)
            val paint = Paint()
            val rect =
                Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            val roundPx = cornerRadiusPx.toFloat()
            paint.isDither = true
            paint.isFilterBitmap = true
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }

        fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
            val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val width = newBitmap.width
            val height = newBitmap.height
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            return Bitmap.createBitmap(newBitmap, 0, 0, width, height, matrix, false)
        }

        fun getDrawableWithBorder(color: ColorStateList, borderWidth: Int, borderColor: Int, radius: Float = 0f): GradientDrawable {
            val drawable = GradientDrawable()
            drawable.cornerRadius = radius
            drawable.color = color
            drawable.setStroke(borderWidth, borderColor)
            return drawable
        }

        fun centerPopup(
            constraintLayout: ConstraintLayout?,
            foreground: View,
            background: View = constraintLayout as View,
            top: Int = 0,
            left: Int = 0,
            bottom: Int = 0,
            right: Int = 0
        ) {
            val set = ConstraintSet()
            set.connect(foreground.id, ConstraintSet.TOP, background.id, ConstraintSet.TOP, top)
            set.connect(foreground.id, ConstraintSet.LEFT, background.id, ConstraintSet.LEFT, left)
            set.connect(
                foreground.id,
                ConstraintSet.BOTTOM,
                background.id,
                ConstraintSet.BOTTOM,
                bottom
            )
            set.connect(
                foreground.id,
                ConstraintSet.RIGHT,
                background.id,
                ConstraintSet.RIGHT,
                right
            )
            set.applyTo(constraintLayout)
        }

        fun anchorToBottom(constraintLayout: ConstraintLayout, foreground: View, background: View = constraintLayout as View, bottom: Int = 0){
            val set = ConstraintSet()
            set.clone(constraintLayout)

            set.connect(foreground.id, ConstraintSet.BOTTOM, background.id, ConstraintSet.BOTTOM, bottom)

            set.connect(foreground.id, ConstraintSet.LEFT, background.id, ConstraintSet.LEFT, 0)
            set.connect(foreground.id, ConstraintSet.RIGHT, background.id, ConstraintSet.RIGHT, 0)
            set.setHorizontalChainStyle(foreground.id, ConstraintSet.CHAIN_SPREAD)

            set.applyTo(constraintLayout)
        }

        fun getStateList(color: Int) : ColorStateList {
            return getStateList(color, color, color, color)
        }

        fun getStateList(enabled: Int, disabled: Int, checked: Int, pressed: Int): ColorStateList {
            val states = arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(
                    -android.R.attr.state_checked
                ),
                intArrayOf(android.R.attr.state_pressed)
            )
            val colors = intArrayOf(
                enabled,
                disabled,
                checked,
                pressed
            )
            return ColorStateList(states, colors)
        }

        fun repaint(image: Bitmap, color: Int) {
            val iconPaint = Paint()
            iconPaint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
            val canvas = Canvas(image)
            canvas.drawBitmap(image, Matrix(), iconPaint)
        }

        fun overlayMapPin(bmp1: Bitmap, bmp2: Bitmap, p1: Paint?, p2: Paint?): Bitmap {
            val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
            val canvas = Canvas(bmOverlay)
            canvas.drawBitmap(bmp1, Matrix(), p1)
            canvas.drawBitmap(bmp2, (bmp1.width / 2 - bmp2.width / 2).toFloat(), (bmp1.height * 13 / 40 - bmp2.height * 13 / 40).toFloat(), p2)
            return bmOverlay
        }

        fun compressBitmapToFile(bitmap: Bitmap, fileUrl: URI, sizeLimit: Long = 1_000_000_000_000,
                                 compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG) : Boolean{
            val imageFile = File(fileUrl)

            if(!imageFile.exists()) {
                val creationSuccess = imageFile.createNewFile()

                if(!creationSuccess) {
                    Log.d("CompressBitmapToFile", "Error, could not create new temporary file")
                    return false
                }
            }

            val fileOutputStream = FileOutputStream(imageFile)
            bitmap.compress(compressFormat, 100, fileOutputStream)

            var quality = 100

            while (imageFile.length() > sizeLimit && quality >= 0) {
                val outputStream = FileOutputStream(imageFile)
                bitmap.compress(compressFormat, quality, outputStream)
                quality -= 5
            }

            return !(quality < 0 && imageFile.length() > sizeLimit)
        }

        fun addCircularBorderToBitmap(context: Context, bitmap: Bitmap, borderWidthDp: Int, borderColor: Int): Bitmap? {
            var width = bitmap.width
            if (width % 2 != 0) width++
            var height = bitmap.height
            if (height % 2 != 0) height++
            var borderWidth: Int = dpToPx(context, borderWidthDp)
            if (borderWidth % 2 != 0) borderWidth++
            val roundedBitmap: Bitmap?= makeRoundBitmap(bitmap, bitmap.width / 2)
            var resizedBitmap: Bitmap? =
                roundedBitmap?.let {
                    getResizedBitmap(it, width - borderWidth, height - borderWidth)
                }
            val backgroundBitmap: Bitmap = colorToBitmap(width, height, borderColor)
            val background: Bitmap = drawCircularHole(backgroundBitmap, backgroundBitmap.width / 2, borderWidth)
            resizedBitmap = resizedBitmap?.let { overlayBitmap(it, background, null, null) }
            return resizedBitmap
        }

        fun drawCircularHole(background: Bitmap?, radius: Int, borderWidth: Int): Bitmap {
            val canvas = Canvas(background!!)
            val paint = Paint()
            paint.color = Color.TRANSPARENT
            paint.isDither = true
            paint.isFilterBitmap = true
            paint.isAntiAlias = true
            paint.style = Paint.Style.FILL
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            canvas.drawARGB(0, 0, 0, 0)
            canvas.drawCircle(radius.toFloat(), radius.toFloat(), (radius - borderWidth).toFloat(), paint)
            return background
        }

        fun overlayBitmap(bmp1: Bitmap, bmp2: Bitmap, p1: Paint?, p2: Paint?): Bitmap {
            val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
            val canvas = Canvas(bmOverlay)
            canvas.drawBitmap(bmp1, Matrix(), p1)
            canvas.drawBitmap(bmp2, (bmp1.width / 2 - bmp2.width / 2).toFloat(), (bmp1.height / 2 - bmp2.height / 2).toFloat(), p2)
            return bmOverlay
        }

        /*fun addBorderToBitmap(context: Context, bitmap: Bitmap, borderWidthDp: Int, borderColor: Int): Bitmap {
            var width = bitmap.width
            if (width % 2 != 0) width++
            var height = bitmap.height
            if (height % 2 != 0) height++
            var borderWidth: Int = dpToPx(context, borderWidthDp)
            if (borderWidth % 2 != 0) borderWidth++
            val roundedBitmap: Bitmap = roundBitmap(bitmap, 10 - borderWidthDp / 2)
            var resizedBitmap: Bitmap = roundedBitmap.let { getResizedBitmap(it, width - borderWidth, height - borderWidth) }
            val backgroundBitmap: Bitmap = colorToBitmap(width, height, borderColor)
            val background: Bitmap = drawHole(backgroundBitmap, borderWidth / 2, dpToPx(context, (10 - borderWidthDp / 2)))
            resizedBitmap = overlayBitmap(background, resizedBitmap, null, null)
            return resizedBitmap
        }*/

        fun drawHole(background: Bitmap, delta: Int, cornerRadiusPx: Int): Bitmap {
            val canvas = Canvas(background)
            val paint = Paint()
            paint.color = Color.TRANSPARENT
            paint.isDither = true
            paint.isFilterBitmap = true
            paint.isAntiAlias = true
            paint.style = Paint.Style.FILL
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            canvas.drawARGB(0, 0, 0, 0)
            val rectF = RectF(
                delta.toFloat(),
                delta.toFloat(),
                (background.width - delta).toFloat(),
                (background.height - delta).toFloat()
            )
            canvas.drawRoundRect(rectF, cornerRadiusPx.toFloat(), cornerRadiusPx.toFloat(), paint)
            return background
        }

        fun darkenBitmap(bm: Bitmap): Bitmap {
            val canvas = Canvas(bm)
            val p = Paint(Color.RED)
            val filter: ColorFilter = LightingColorFilter(-0x808081, 0x00000000) // darken
            p.colorFilter = filter
            canvas.drawBitmap(bm, Matrix(), p)
            return bm
        }

        fun darkenBitmapMedium(bm: Bitmap): Bitmap {
            val canvas = Canvas(bm)
            val p = Paint(Color.RED)
            val filter: ColorFilter = LightingColorFilter(-0x404040, 0x00000000) // darken
            p.colorFilter = filter
            canvas.drawBitmap(bm, Matrix(), p)
            return bm
        }

        /*fun makeWordsClickable(textView: TextView, hyperText: String, colorString: String, runnable: Runnable){
            val devFull = hyperText.split("<font color='#$colorString'>").toTypedArray()

            textView.movementMethod = LinkMovementMethod.getInstance()

            textView.append(HtmlCompat.fromHtml(devFull[0], HtmlCompat.FROM_HTML_MODE_LEGACY))

            val link = arrayOfNulls<SpannableString>(devFull.size - 1)

            val cs = arrayOfNulls<ClickableSpan>(devFull.size - 1)
            var linkWord: String

            var devDevFull: Array<String>

            for (i in 1 until devFull.size) {
                devDevFull = devFull[i].split("</font>").toTypedArray()
                link[i - 1] = SpannableString(devDevFull[0])
                linkWord = devDevFull[0]
                cs[i - 1] = object : CustomClickableSpan(Color.parseColor("#$colorString")){
                    override fun onClick(p0: View) {
                        runnable.run()
                    }
                }
                link[i - 1]!!.setSpan(cs[i - 1], 0, linkWord.length, 0)
                textView.append(link[i - 1])
                try {
                    textView.append(
                        HtmlCompat.fromHtml(
                            devDevFull[1],
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        ).toString() + " "
                    )
                } catch (e: Exception) {
                    Log.d("MakeWordsClickable", "Error, could not parse hypertext", e)
                }
            }
        }*/

        fun getScreenWith(activity: Activity?): Int? {
            if (activity==null){ return null }
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = activity.windowManager.currentWindowMetrics
                val insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.width() - insets.left - insets.right
            } else {
                val displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
        }
    }
}