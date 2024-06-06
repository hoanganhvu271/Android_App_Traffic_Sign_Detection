package com.hav.group3.Yolo

data class BoundingBox(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val cx: Float,
    val cy: Float,
    val w: Float,
    val h: Float,
    val cnf: Float,
    val cls: Int,
    val clsName: String
)
data class TrackableObject(val id: Int, var boundingBox: BoundingBox, var age: Int = 0, var lost: Boolean = false, var isFirstDetected: Boolean = true)

class Tracker() {
    private val objects = mutableListOf<TrackableObject>()
    private var nextId = 0

    fun update(detections: List<BoundingBox>) {
        val matchedDetections = mutableSetOf<BoundingBox>()

        for (obj in objects) {
            obj.age += 1
            val bestMatch = detections.maxByOrNull { calculateIoU(obj.boundingBox, it) }
            if (bestMatch != null && calculateIoU(obj.boundingBox, bestMatch) > IOU_THRESHOLD) {
                obj.boundingBox = bestMatch
                obj.age = 0
                matchedDetections.add(bestMatch)
            } else {
                obj.lost = true
            }
        }

        val newDetections = detections.filter { it !in matchedDetections }
        for (detection in newDetections) {
            objects.add(TrackableObject(nextId++, detection))
        }

        objects.removeAll { it.lost }
    }

    fun getTrackedObjects(): List<TrackableObject> {
        return objects
    }

    private fun calculateIoU(box1: BoundingBox, box2: BoundingBox): Float {
        val x1 = maxOf(box1.x1, box2.x1)
        val y1 = maxOf(box1.y1, box2.y1)
        val x2 = minOf(box1.x2, box2.x2)
        val y2 = minOf(box1.y2, box2.y2)
        val intersectionArea = maxOf(0F, x2 - x1) * maxOf(0F, y2 - y1)
        val box1Area = box1.w * box1.h
        val box2Area = box2.w * box2.h
        return intersectionArea / (box1Area + box2Area - intersectionArea)
    }

    companion object {
        private const val IOU_THRESHOLD = 0.3F
    }
}
