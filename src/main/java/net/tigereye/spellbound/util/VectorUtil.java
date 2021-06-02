package net.tigereye.spellbound.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tigereye.spellbound.Spellbound;

import java.util.Collections;

public class VectorUtil {

    public static BlockExitInfo getHorizontalExitPoint(Vec3d position, Vec3d velocity, Vec3d borders){
        double timeToImpactX;
        if(velocity.x != 0){
            timeToImpactX = (borders.x-position.x)/ velocity.x;
        }
        else{timeToImpactX = Double.POSITIVE_INFINITY;}

        double timeToImpactZ;
        if(velocity.z != 0){
            timeToImpactZ = (borders.z-position.z)/ velocity.z;
        }
        else{timeToImpactZ = Double.POSITIVE_INFINITY;}

        double timeToImpact;
        BlockExitInfo output = new BlockExitInfo();
        if(Double.isInfinite(timeToImpactX) && Double.isInfinite(timeToImpactZ)){
            return null;
        }
        if(timeToImpactX <= timeToImpactZ){
            //x intersects first
            timeToImpact = timeToImpactX;
            output.direction = (velocity.x >= 0) ? Direction.EAST : Direction.WEST;
        }
        else{
            //z intersects first
            timeToImpact = timeToImpactZ;
            output.direction = (velocity.z >= 0) ? Direction.SOUTH : Direction.NORTH;
        }

        output.pos = position.add(velocity.multiply(timeToImpact));
        if(Spellbound.DEBUG){
            Spellbound.LOGGER.info("TTX: "+timeToImpactX+" TTZ: "+timeToImpactZ);
            Spellbound.LOGGER.info("Border crossed: "+output.direction.name()+" "+output.pos.getX()+","+output.pos.getY()+","+output.pos.getZ());
        }
        return output;
    }

    public static Vec3d findCollisionWithStepAssistOnLine(World world, Vec3d position, Vec3d direction, double length){
        int remainingMaxIterations = (int)(length*2);
        BlockPos blockPos = new BlockPos(position);
        Vec3d unitVector = direction.normalize();
        Vec3d finalPosition = position.add(unitVector.multiply(length/*level*DISTANCE_PER_LEVEL*/));
        BlockPos finalBlockPosition = new BlockPos(finalPosition);
        boolean endPointFound = false;
        while(!endPointFound) {
            //find point and direction where next block is touched
            Vec3d borders = new Vec3d(unitVector.x > 0 ? blockPos.getX()+1 : blockPos.getX(), blockPos.getY(), unitVector.z > 0 ? blockPos.getZ()+1 : blockPos.getZ());
            if(Spellbound.DEBUG){
                Spellbound.LOGGER.info("Tracked Borders: "+borders.getX()+","+borders.getY()+","+borders.getZ());
            }
            VectorUtil.BlockExitInfo exitInfo = VectorUtil.getHorizontalExitPoint(position,unitVector,borders);
            if(exitInfo == null){
                return position;
            }
            position = exitInfo.pos;
            blockPos = blockPos.offset(exitInfo.direction);
            if(Spellbound.DEBUG){
                Spellbound.LOGGER.info("Leap line entering block "+blockPos.getX()+","+blockPos.getY()+","+blockPos.getZ());
            }
            //check for obstruction, attempt to go over it
            if(SpellboundUtil.isPositionObstructed(world,blockPos)){
                blockPos = blockPos.offset(Direction.UP);
                if(SpellboundUtil.isPositionObstructed(world,blockPos)){
                    endPointFound = true;
                    blockPos = blockPos.offset(exitInfo.direction,-1);
                    position = new Vec3d(blockPos.getX()+.5, blockPos.getY()-1, blockPos.getZ()+.5 );
                }
                else{
                    //move position and end position up one block
                    position = new Vec3d(position.x,Math.floor(position.y+1),position.z);
                    finalPosition = finalPosition.add(0,1,0);
                    finalBlockPosition = finalBlockPosition.add(0,1,0);
                }
            }
            //check if finalBlockPosition has been reached
            if(!endPointFound && blockPos.equals(finalBlockPosition)){
                //if so, set position to finalPosition and get ready to warp
                endPointFound = true;
                position = finalPosition;
            }
            //finally, a sanity check to make sure we don't fly away into space somehow
            remainingMaxIterations--;
            if(remainingMaxIterations <= 0){
                Spellbound.LOGGER.error("Leap hit max iterations.");
                endPointFound = true;
            }
        }
        if(Double.isNaN(position.x)||Double.isNaN(position.y)||Double.isNaN(position.z)){
            Spellbound.LOGGER.error("Position returned NaN, that is very dangerous! Please report to dev.");
            return null;
        }
        return position;
    }

    public static Vec3d roundVectorAxis(Vec3d pos,Direction.Axis axis){
        switch(axis){
            case X:
                return new Vec3d(Math.round(pos.x),pos.y,pos.z);
            case Y:
                return new Vec3d(pos.x,Math.round(pos.y),pos.z);
            case Z:
                return new Vec3d(pos.x,pos.y,Math.round(pos.z));
        }
        Spellbound.LOGGER.warn("Vector rounding failed, axis not found.");
        return pos;
    }

    public static class BlockExitInfo{
        public Vec3d pos;
        public Direction direction;
    }
}
